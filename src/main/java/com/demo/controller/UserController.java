package com.demo.controller;

import com.demo.dto.Message;
import com.demo.security.JwtUtils;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@RequestHeader("token") String token) {
        try {
            boolean isValid = jwtUtils.validateJwtToken(token);
            if (isValid) {
                return ResponseEntity.ok(userService.getUserByEmail(jwtUtils.getUserNameFromJwtToken(token)));
            }
            return ResponseEntity.badRequest().body(new Message("permission denied"));
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }

    @PostMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROOT') or hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(@RequestHeader("token") String token,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(value = "limit", required = false, defaultValue = "12") int limit) {
        try {
            boolean isValid = jwtUtils.validateJwtToken(token);
            if (isValid) return ResponseEntity.ok(userService.getUsersInPage(page, limit));
            return ResponseEntity.badRequest().body(new Message("permission denied"));
   } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }
}
