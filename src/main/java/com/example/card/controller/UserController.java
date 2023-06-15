package com.example.card.controller;

import com.example.card.domain.User;
import com.example.card.domain.UserResponse;
import com.example.card.service.UserService;
import com.example.card.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody User user1) {
        User user=userService.getUserByEmail(user1.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(user1.getPassword(), user.getPassword())) {
            String token = JwtUtil.generateToken(user1.getEmail(), user.getRole());
            user.setToken(token);
            UserResponse userResponse= UserResponse.builder()
                    .email(user1.getEmail())
                    .role(user.getRole().name())
                    .token(token)
                    .build();
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
         user.setPassword(passwordEncoder.encode(user.getPassword()));
         userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
