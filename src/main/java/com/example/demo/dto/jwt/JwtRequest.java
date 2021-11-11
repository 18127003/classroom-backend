package com.example.demo.dto.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@RequiredArgsConstructor
public class JwtRequest implements Serializable {
    private String email;
    private String password;
}
