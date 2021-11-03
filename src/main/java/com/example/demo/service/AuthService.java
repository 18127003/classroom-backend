package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    boolean validatePassword(String rawPassword, String password);
}
