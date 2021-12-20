package com.example.demo.service.impl;

import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.dto.jwt.JwtRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AuthService;
import com.example.demo.util.PasswordUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final AdminRepository adminRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final PasswordUtil passwordUtil;


    @Override
    public Account validatePassword(JwtRequest jwtRequest) {
        var user = accountRepository.findByEmail(jwtRequest.getEmail());
        if(user==null){
            throw new RTException(new RecordNotFoundException(jwtRequest.getEmail(), Account.class.getSimpleName()));
        }
        return passwordUtil.checkPassword(jwtRequest.getPassword(), user.getPassword())?user:null;
    }

    @Override
    public Account validateSocialToken(String tokenId) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken;
        idToken = googleIdTokenVerifier.verify(tokenId);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // find account in app db
            var account = accountRepository.findByEmail(email);
            if(account==null){
                return accountRepository.save(new Account(familyName, givenName, "", email));
            }
            return account;
        } else {
            System.out.println("Invalid ID token.");
            return null;
        }
    }

    @Override
    public Admin validatePasswordAdmin(JwtRequest jwtRequest) {
        var user = adminRepository.findByEmail(jwtRequest.getEmail());
        if(user==null){
            throw new RTException(new RecordNotFoundException(jwtRequest.getEmail(), Admin.class.getSimpleName()));
        }
        return passwordUtil.checkPassword(jwtRequest.getPassword(), user.getPassword())?user:null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return accountRepository.findById(Long.valueOf(s))
                .orElseThrow(() -> new UsernameNotFoundException("Not found user name "+ s));
    }
}
