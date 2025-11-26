package com.example.demo.service;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class AuthService {

    private final PersonRepository personRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(PersonRepository personRepo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.personRepo = personRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public Person register(Person p) {
        p.setPassword(encoder.encode(p.getPassword()));
        if (p.getRoles() == null || p.getRoles().isEmpty()) {
            p.setRoles(Set.of("ROLE_STUDENT"));
        }
        return personRepo.save(p);
    }

    public String loginToken(Person p) {
        var person = personRepo.findByUsername(p.getUsername()).orElseThrow();
        return jwtUtil.generateToken(person.getUsername(), person.getRoles().stream().toList());
    }
}
