package com.example.demo.service.impl;

import com.example.demo.common.exception.RTException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean validatePassword(String rawPassword, String password) {
        if (rawPassword.isEmpty()&&password.isEmpty()){
            return true;
        }
        if (rawPassword.isEmpty()||password.isEmpty()){
            return false;
        }
        return passwordEncoder.matches(rawPassword, password);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = accountRepository.findByName(s);
        if(user==null){
            throw new UsernameNotFoundException("Not found user name "+ s);
        }
        return user;
    }
}
