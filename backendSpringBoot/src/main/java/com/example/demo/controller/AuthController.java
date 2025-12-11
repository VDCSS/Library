package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.AuthService;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){this.auth=auth;}
    @PostMapping("/register")
    public Map<String,String> register(@RequestBody Map<String,String> body){ return auth.register(body.get("name"), body.get("email"), body.get("password"), body.get("role")); }
    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> body){ return auth.login(body.get("email"), body.get("password")); }
}
