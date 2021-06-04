package com.demo.service;

import com.demo.dao.RoleDAO;
import com.demo.dao.UserDAO;
import com.demo.dto.AuthRequest;
import com.demo.dto.Message;
import com.demo.dto.ChangePasswordRequest;
import com.demo.model.Role;
import com.demo.model.User;
import com.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Objects;

@Service
@Transactional
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 PasswordEncoder encoder, JwtUtils jwtUtils,
                                 UserDAO userDAO, RoleDAO roleDAO) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    public ResponseEntity<?> login(AuthRequest loginRequest) {
        try {
            if (loginRequest.getEmail().equals("admin@gmail.com") && loginRequest.getPassword().equals("password")) {
                User rootUser = userDAO.getUserByEmailAddress(loginRequest.getEmail());
                if(rootUser == null) {
                    rootUser = new User(loginRequest.getEmail(), encoder.encode(loginRequest.getPassword()));
                    userDAO.merge(rootUser);
                }
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail().trim(), loginRequest.getPassword().trim()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new Message("Đăng nhập thành công!", jwtUtils.generateJwtToken(authentication)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Message("wrong email or password"));
        }
    }

    public ResponseEntity<?> changePassword(ChangePasswordRequest passwordRequest, String token) {
        try {
            User user = userDAO.getUserByEmailAddress(jwtUtils.getUserNameFromJwtToken(token));
            if (encoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
                user.setPassword(encoder.encode(passwordRequest.getNewPassword()));
                userDAO.merge(user);
                return ResponseEntity.ok(new Message("Thay đổi mật khẩu thành công!"));
            } else {
                return ResponseEntity.badRequest().body(new Message("wrong password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }

    public ResponseEntity<?> register(AuthRequest authRequest, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity
                    .badRequest()
                    .body(new Message(Objects.requireNonNull(result.getFieldError()).getDefaultMessage()));
        if (userDAO.isEmailAddressUsed(authRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new Message("email is already exist"));
        }

        // int n = 100000 + random_float() * 900000;
        // Create new user's account
        User user = new User(
                authRequest.getEmail(),
                encoder.encode(authRequest.getPassword()));

        Role rootRole = roleDAO.getById(Role.class, 1);
        if(rootRole == null) {
            rootRole = new Role();
            rootRole.setId(1);
            rootRole.setName("ROLE_ROOT");
            roleDAO.merge(rootRole);
        }

        Role adminRole = roleDAO.getById(Role.class, 2);
        if(adminRole == null) {
            adminRole = new Role();
            adminRole.setId(2);
            adminRole.setName("ROLE_ADMIN");
            roleDAO.merge(adminRole);
        }

        Role userRole = roleDAO.getById(Role.class, 3);
        if(userRole == null) {
            userRole = new Role();
            userRole.setId(3);
            user.setName("ROLE_USER");
            roleDAO.merge(userRole);
        }

        HashSet<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userDAO.save(user);
        return ResponseEntity.ok(new Message("Đăng ký thành công!"));
    }
}
