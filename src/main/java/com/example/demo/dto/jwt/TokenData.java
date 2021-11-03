package com.example.demo.dto.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenData {
    private String sessionId;
    private boolean valid;
    private Date expiryDate;
}
