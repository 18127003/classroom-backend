package com.example.demo;

import com.example.demo.security.ApiSecurityConfig;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

import static com.example.demo.common.constant.Constants.GOOGLE_AUTH_CLIENT_ID;

@SpringBootApplication(scanBasePackageClasses = WebConfig.class)
@Import({ApiSecurityConfig.class})
@Slf4j
public class WebConfig {
    @Value("${com.demo.password}")
    private String password;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:8085",
                                "https://18127003.github.io",
                                "https://mnclassroom.netlify.app")
                        .allowedMethods("PUT","DELETE","GET","POST","PATCH")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            // Specify the CLIENT_ID of the app that accesses the backend:
            .setAudience(Collections.singletonList(GOOGLE_AUTH_CLIENT_ID))
            // Or, if multiple clients access the backend:
            //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
            .build();
    }

    @Primary
    @Bean(name = "encryptorBean")
    public StringEncryptor stringEncryptor() {
        final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
