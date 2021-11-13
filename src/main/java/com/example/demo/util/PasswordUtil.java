package com.example.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    public boolean checkPassword(String rawPassword, String password) {
        if (rawPassword.isEmpty()&&password.isEmpty()){
            return true;
        }
        if (rawPassword.isEmpty()||password.isEmpty()){
            return false;
        }
        return passwordEncoder.matches(rawPassword, password);
    }

    public String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }
}
