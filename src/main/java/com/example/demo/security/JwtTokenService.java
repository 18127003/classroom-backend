package com.example.demo.security;

import com.example.demo.dto.jwt.TokenData;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtTokenService {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    @Value("${auth.jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${auth.jwt.secretKey}")
    private String secretKey;

    private List<TokenData> tokenData = new LinkedList<>();

    public String generateJwtToken(UserDetails userDetails) {
        String jwtSessionId = UUID.randomUUID().toString();
        Date now = new Date();
        Date expiration = DateUtils.addMilliseconds(now, jwtExpirationMs);
        tokenData.add(new TokenData(jwtSessionId, true, expiration));
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setId(jwtSessionId)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getSessionIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Optional.ofNullable(getAllClaimsFromToken(token))
                .orElseThrow();
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (SignatureException e){
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e){
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        throw new JwtException("Can't extract claims from the token");
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserIdFromToken(token);
        boolean usernameValid = username.equals(userDetails.getUsername());
        boolean tokenNotExpired = !isTokenExpired(token);
        boolean tokenNotRefreshed = tokenData.stream()
                .anyMatch(tokenItem -> tokenItem.getSessionId().equals(getSessionIdFromToken(token))
                        && tokenItem.isValid());
        return usernameValid && tokenNotExpired && tokenNotRefreshed;
    }

    public void invalidToken(String tokenId) {
        tokenData.stream()
                .filter(tokenItem -> tokenItem.getSessionId().equals(tokenId))
                .findAny()
                .ifPresent(tokenItem -> tokenItem.setValid(false));
    }
}
