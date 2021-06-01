package com.demo.controller;

import com.demo.dto.AuthRequest;
import com.demo.dto.ChangePasswordRequest;
import com.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController extends AbstractController {

    private final AuthenticationService authenticate;

    @Autowired
    public AuthenticationController(AuthenticationService authenticate) {
        this.authenticate = authenticate;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest loginRequest) {
        return authenticate.login(loginRequest);
    }

    @PostMapping(value = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest resetPasswordRequest,
                                            @RequestHeader("token") String token) {
        return authenticate.changePassword(resetPasswordRequest, token);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest authRequest, BindingResult result) {
        return authenticate.register(authRequest, result);
    }
}
