package com.example.demo.controller;

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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
        final var userDetails = authService.loadUserByUsername(jwtRequest.getUsername());
        if (authService.validatePassword(jwtRequest.getPassword(), userDetails.getPassword())) {
//            final var cookie = new Cookie(JWT_COOKIE_TEXT, jwtService.generateJwtToken(userDetails));
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(cookieMaxAge);
//            cookie.setPath(WEBAPP_API_PATH);
//            response.addCookie(cookie);
            ResponseCookie b = ResponseCookie.from(JWT_COOKIE_TEXT, jwtService.generateJwtToken(userDetails))
                    .maxAge(cookieMaxAge)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path(WEBAPP_API_PATH)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, b.toString());
            return ResponseEntity.ok(accountMapper.toAccountDto((Account) userDetails));
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
}
