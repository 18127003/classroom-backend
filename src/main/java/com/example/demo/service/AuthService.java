package com.example.demo.service;

import com.example.demo.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService extends UserDetailsService {
    boolean validatePassword(String rawPassword, String password);

    Account validateSocialToken(String tokenId) throws GeneralSecurityException, IOException;
}
