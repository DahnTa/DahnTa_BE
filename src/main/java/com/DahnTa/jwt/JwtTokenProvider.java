package com.DahnTa.jwt;

import com.DahnTa.entity.Enum.JwtClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    public String generateAccessToken(Long userId, String userAccount) {
        return Jwts.builder()
            .claim(JwtClaims.USER_ID.getValue(), userId)
            .claim(JwtClaims.USER_ACCOUNT.getValue(), userAccount)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenValidity()))
            .signWith(getSigningKey())
            .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
            .claim(JwtClaims.USER_ID.getValue(), userId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenValidity()))
            .signWith(getSigningKey())
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return claims.get(JwtClaims.USER_ID.getValue(), Long.class);
    }
}
