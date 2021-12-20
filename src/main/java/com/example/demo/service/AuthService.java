package com.example.demo.service;

import com.example.demo.dto.jwt.JwtRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService extends UserDetailsService {
    Account validatePassword(JwtRequest jwtRequest);

    Account validateSocialToken(String tokenId) throws GeneralSecurityException, IOException;

    Admin validatePasswordAdmin(JwtRequest jwtRequest);
}
