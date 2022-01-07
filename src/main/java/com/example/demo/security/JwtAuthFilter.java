package com.example.demo.security;

import com.example.demo.controller.AbstractServiceEndpoint;
import com.example.demo.service.AccountService;
import com.example.demo.service.AuthService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.example.demo.common.constant.Constants.JWT_COOKIE_TEXT;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var cookies = request.getCookies();
        if (cookies==null){
            filterChain.doFilter(request, response);
            return;
        }
        var jwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> JWT_COOKIE_TEXT.equals(cookie.getName()))
                .findAny()
                .orElse(null);
        if (jwtCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            var jwtToken = jwtCookie.getValue();
            var userId = jwtService.validateToken(jwtToken);
            if (userId != null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        Long.valueOf(userId), null, null);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (UsernameNotFoundException | JwtException exception){
            System.out.println("Here");
            final var cookie = new Cookie(JWT_COOKIE_TEXT, null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath(AbstractServiceEndpoint.WEBAPP_API_PATH);
            response.addCookie(cookie);
        }

        filterChain.doFilter(request, response);
    }
}
