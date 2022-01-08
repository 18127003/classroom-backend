package com.example.demo.security;

import com.example.demo.common.exception.RTException;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class AdminAccessFilter extends OncePerRequestFilter {
    public static final String ADMIN_URL_PATTERN = "**/admin/**";

    @Autowired
    private AntPathMatcher antPathMatcher;

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var requestUrl = httpServletRequest.getRequestURL().toString();
        if(antPathMatcher.match(ADMIN_URL_PATTERN, requestUrl)){
            try{
                UUID accountId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if(adminService.checkExist(accountId)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            accountId,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            } catch (RTException e){
                // ignore because unauthorized later
                System.out.println("Admin unauthorized");
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
