package com.example.demo.controller;

import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.jwt.JwtRequest;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.security.JwtTokenService;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import static com.example.demo.common.constant.Constants.JWT_COOKIE_TEXT;

@RestController
@RequestMapping(AbstractServiceEndpoint.AUTH_PATH)
@RequiredArgsConstructor
public class AuthController extends AbstractServiceEndpoint{
    private final JwtTokenService jwtService;
    private final AuthService authService;
    private final AccountMapper accountMapper;

    @Value("${com.demo.cookieMaxAge}")
    private int cookieMaxAge;

    @PostMapping(value = "login")
    public ResponseEntity<AccountDto> authenticate(@RequestBody final JwtRequest jwtRequest, final HttpServletResponse response) {
        try{
            var user = authService.validatePassword(jwtRequest);
            response.addHeader(HttpHeaders.SET_COOKIE, generateJwtCookies(user).toString());
            return ResponseEntity.ok(accountMapper.toAccountDto(user));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "socialLogin")
    public ResponseEntity<AccountDto> socialAuthenticate(@RequestBody final String tokenId, final HttpServletResponse response){

        try {
            var account = authService.validateSocialToken(tokenId);
            if (account != null){
                response.addHeader(HttpHeaders.SET_COOKIE, generateJwtCookies(account).toString());
                return ResponseEntity.ok(accountMapper.toAccountDto(account));
            }

        } catch (GeneralSecurityException | IOException ignored) {

        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response){
        final var cookie = new Cookie(JWT_COOKIE_TEXT, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath(WEBAPP_API_PATH);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/revokeToken/{tokenId}")
    public ResponseEntity<Boolean> revokeToken(@PathVariable final String tokenId) {
        jwtService.invalidToken(tokenId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    private ResponseCookie generateJwtCookies(UserDetails account){
        return ResponseCookie.from(JWT_COOKIE_TEXT, jwtService.generateJwtToken(account))
                .maxAge(cookieMaxAge)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(WEBAPP_API_PATH)
                .build();
    }
}
