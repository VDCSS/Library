package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PersonRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(PersonRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Person payload) {
        if (repo.findByUsername(payload.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username_taken"));
        }
        payload.setPassword(passwordEncoder.encode(payload.getPassword()));
        if (payload.getRoles() == null || payload.getRoles().isEmpty()) {
            payload.setRoles(Set.of("ROLE_STUDENT"));
        }
        Person saved = repo.save(payload);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest body) {
        var opt = repo.findByUsername(body.getUsername());
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","invalid_credentials"));
        Person p = opt.get();
        if (!passwordEncoder.matches(body.getPassword(), p.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error","invalid_credentials"));
        }
        var roles = p.getRoles();
        String token = jwtUtil.generateToken(p.getUsername(), roles.stream().toList());
        AuthResponse resp = AuthResponse.builder().token(token).roles(roles).id(p.getId()).username(p.getUsername()).build();
        return ResponseEntity.ok(resp);
    }
}
