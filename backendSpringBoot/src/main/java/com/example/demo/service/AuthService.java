package com.example.demo.service;

import java.util.Map;

public interface AuthService {
    Map<String,String> register(String name, String email, String password, String role);
    Map<String,String> login(String email, String password);
}
