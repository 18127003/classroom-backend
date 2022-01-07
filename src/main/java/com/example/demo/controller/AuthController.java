package com.example.demo.controller;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.AdminDto;
import com.example.demo.dto.jwt.JwtRequest;
import com.example.demo.dto.jwt.SignInResponse;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.security.JwtTokenService;
import com.example.demo.service.AuthService;
import com.example.demo.service.VerifyTokenService;
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
    private final VerifyTokenService verifyTokenService;
    private final AccountMapper accountMapper;
    private final AdminMapper adminMapper;

    @Value("${com.demo.cookieMaxAge}")
    private int cookieMaxAge;

    @Value("${auth.jwt.refreshTokenExpirationM}")
    private int refreshTokenExpiry;

    @PostMapping(value = "login")
    public ResponseEntity<SignInResponse> authenticate(@RequestBody final JwtRequest jwtRequest, final HttpServletResponse response) {
        var user = authService.validatePassword(jwtRequest);
        response.addHeader(HttpHeaders.SET_COOKIE, generateJwtCookies(user).toString());
        var refreshToken = verifyTokenService.getOrCreateToken(user, VerifyTokenType.REFRESH_TOKEN, refreshTokenExpiry);
        return ResponseEntity.ok(new SignInResponse(accountMapper.toAccountDto(user), refreshToken.getToken()));
    }

    @PostMapping(value = "loginAdmin")
    public ResponseEntity<AdminDto> authenticateAdmin(@RequestBody final JwtRequest jwtRequest, final HttpServletResponse response) {
        var user = authService.validatePasswordAdmin(jwtRequest);
        response.addHeader(HttpHeaders.SET_COOKIE, generateJwtCookies(user).toString());
        return ResponseEntity.ok(adminMapper.toAdminDto(user));
    }

    @PostMapping(value = "socialLogin")
    public ResponseEntity<SignInResponse> socialAuthenticate(@RequestBody final String tokenId, final HttpServletResponse response){

        try {
            var account = authService.validateSocialToken(tokenId);
            if (account != null){
                response.addHeader(HttpHeaders.SET_COOKIE, generateJwtCookies(account).toString());
                var refreshToken = verifyTokenService.getOrCreateToken(account, VerifyTokenType.REFRESH_TOKEN, refreshTokenExpiry);
                return ResponseEntity.ok(new SignInResponse(accountMapper.toAccountDto(account), refreshToken.getToken()));
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

    @GetMapping("testConnection")
    public ResponseEntity<Void> testConnection(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/refreshToken/{tokenString}")
    public ResponseEntity<String> refreshToken(@PathVariable final String tokenString) {
        var token = verifyTokenService.verifyToken(tokenString);
        var newToken = verifyTokenService.rotateVerifyToken(token, refreshTokenExpiry);
        return ResponseEntity.ok(newToken.getToken());

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
