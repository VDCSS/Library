package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import com.example.demo.model.Role;
import com.example.demo.security.JwtUtils;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtils jwt;
    public AuthService(UserRepository r, JwtUtils jwt){this.userRepo=r; this.jwt=jwt;}

    public Map<String,String> register(String name,String email,String password,String roleStr){
        if(userRepo.findByEmail(email).isPresent()) throw new RuntimeException("Email exists");
        Role role = Role.valueOf(roleStr);
        User u = User.builder()
                .name(name).email(email)
                .password(encoder.encode(password))
                .role(role)
                .build();
        userRepo.save(u);
        String token = jwt.generateToken(u.getEmail());
        return Map.of("token",token,"email",u.getEmail());
    }

    public Map<String,String> login(String email,String password){
        User u = userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("Bad credentials"));
        if(!encoder.matches(password,u.getPassword())) throw new RuntimeException("Bad credentials");
        String token = jwt.generateToken(u.getEmail());
        return Map.of("token",token,"email",u.getEmail(), "role", u.getRole().name());
    }
}
