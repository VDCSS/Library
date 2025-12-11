package com.example.demo.service.impl;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.AuthService;
import com.example.demo.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtils jwt;

    public AuthServiceImpl(UserRepository userRepo, JwtUtils jwt) {
        this.userRepo = userRepo;
        this.jwt = jwt;
    }

    @Override
    public Map<String, String> register(String name, String email, String password, String roleStr) {
        if (userRepo.findByEmail(email).isPresent()) throw new BadRequestException("Email already registered");
        Role role;
        try { role = Role.valueOf(roleStr); } catch (Exception e) { role = Role.STUDENT; }
        User u = User.builder()
                .name(name)
                .email(email)
                .password(encoder.encode(password))
                .role(role)
                .blocked(false)
                .outstandingFines(0.0)
                .build();
        userRepo.save(u);
        String token = jwt.generateToken(u.getEmail());
        return Map.of("token", token, "email", u.getEmail());
    }

    @Override
    public Map<String, String> login(String email, String password) {
        User u = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!encoder.matches(password, u.getPassword())) throw new BadRequestException("Invalid credentials");
        String token = jwt.generateToken(u.getEmail());
        return Map.of("token", token, "email", u.getEmail(), "role", u.getRole().name());
    }
}
