package com.example.demo.security;

import com.example.demo.controller.AbstractServiceEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.AbstractEndpoint;
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

@Configuration
@EnableWebSecurity
@Slf4j
@Order(0)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtAuthFilter authenticateFilter;

    @Override
    protected final void configure(AuthenticationManagerBuilder auth) {
        // no authentication manager: already done by some filters
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(
                    "/"+ AbstractServiceEndpoint.AUTH_PATH+"login",
                    "/"+ AbstractServiceEndpoint.AUTH_PATH+"socialLogin",
                    "/"+ AbstractServiceEndpoint.ACCOUNT_PATH+"create"
                ).permitAll()
//                .antMatchers(ADMIN_ACCESSIBLE_PATH.toArray(String[]::new)).hasAnyAuthority(Role.ADMIN.name())
//                .antMatchers(INSPECTOR_ACCESSIBLE_PATH.toArray(String[]::new)).hasAnyAuthority(Role.INSPECTOR.name())
                .anyRequest().authenticated();
        http.addFilterBefore(authenticateFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(accessAuthorizeFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
