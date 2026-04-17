package com.locadora.controller;

import com.locadora.domain.entity.User;
import com.locadora.domain.enums.UserRole;
import com.locadora.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String token = authService.authenticate(
            credentials.get("username"),
            credentials.get("password")
        );
        return ResponseEntity.ok(Map.of("token", token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> userData) {
        User user = authService.register(
            userData.get("username"),
            userData.get("email"),
            userData.get("password"),
            UserRole.valueOf(userData.get("role"))
        );
        return ResponseEntity.ok(Map.of("id", user.getId()));
    }
}
