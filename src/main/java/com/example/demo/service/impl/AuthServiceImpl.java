package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

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
    public Account validateSocialToken(String tokenId) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken;
        idToken = googleIdTokenVerifier.verify(tokenId);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

            // find account in app db
            var account = accountRepository.findByName(name);
            if(account==null){
                return accountRepository.save(new Account(name,""));
            }
            return account;
        } else {
            System.out.println("Invalid ID token.");
            return null;
        }
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
