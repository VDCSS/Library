package com.example.demo.controller;

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
            return ResponseEntity.badRequest().body(Map.of("error", "Username taken"));
        }
        payload.setPassword(passwordEncoder.encode(payload.getPassword()));
        if (payload.getRoles() == null || payload.getRoles().isEmpty()) payload.setRoles(Set.of("ROLE_ALUNO"));
        Person saved = repo.save(payload);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Person p = repo.findByUsername(username).orElse(null);
        if (p == null) return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
        if (!passwordEncoder.matches(password, p.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
        }
        String token = jwtUtil.generateToken(p.getUsername(), p.getRoles().stream().toList());
        return ResponseEntity.ok(Map.of("token", token, "roles", p.getRoles(), "username", p.getUsername(), "id", p.getId()));
    }
}
