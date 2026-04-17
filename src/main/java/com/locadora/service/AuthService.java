package com.locadora.service;

import com.locadora.domain.entity.User;
import com.locadora.domain.enums.UserRole;
import com.locadora.repository.UserRepository;
import com.locadora.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return jwtService.generateToken(user);
    }
    
    public User register(String username, String email, String password, UserRole role) {
        User user = User.builder()
            .username(username)
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(role)
            .active(true)
            .build();
        
        return userRepository.save(user);
    }
}
