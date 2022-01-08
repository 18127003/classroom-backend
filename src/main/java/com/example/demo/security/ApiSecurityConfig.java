package com.example.demo.security;

import com.example.demo.common.enums.Role;
import com.example.demo.controller.AbstractServiceEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import static com.example.demo.controller.AbstractServiceEndpoint.*;

@Configuration
@EnableWebSecurity
@Slf4j
@Order(0)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtAuthFilter authenticateFilter;
    private final ApiAccessFilter apiAccessFilter;
    private final AdminAccessFilter adminAccessFilter;

    @Override
    protected final void configure(AuthenticationManagerBuilder auth) {
        // no authentication manager: already done by some filters
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(UNPROTECTED_PATH.toArray(String[]::new)).permitAll()
                .antMatchers(TEACHER_PROTECTED_PATH.toArray(String[]::new)).hasAnyAuthority(Role.TEACHER.name())
                .antMatchers(STUDENT_PROTECTED_PATH.toArray(String[]::new)).hasAnyAuthority(Role.STUDENT.name())
                .antMatchers(ADMIN_PROTECTED_PATH.toArray(String[]::new)).hasAnyAuthority("ADMIN")
                .anyRequest().authenticated();
        http.addFilterBefore(apiAccessFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(adminAccessFilter, ApiAccessFilter.class)
                .addFilterBefore(authenticateFilter, AdminAccessFilter.class);
    }
}
