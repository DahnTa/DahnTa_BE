package com.DahnTa.service;

import com.DahnTa.entity.RefreshToken;
import com.DahnTa.entity.User;
import com.DahnTa.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JWTService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecretKey key;

    public JWTService(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }


    // access token 생성 클래스
    public String generateAccessToken(String email, Long userId) {

        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .claim("email", email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 min
            .signWith(key)
            .compact();
    }


    // refreshToken 생성 메서드
    public String generateRefreshToken(Long userId) {

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14)) // 14 day
            .signWith(key)
            .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token); // 여기서 에러가 안 나면 유효한 토큰임!
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return claims.get("userId", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }


    // @todo : else문 개선 필요
    // @todo : user에 대한 NPE 방지?
    // Refresh Token DB 저장/업데이트
    @Transactional // DB 변경이 일어나므로 트랜잭션 필수
    public void saveToken(User user, String newRefreshTokenStr) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            existingToken.get().updateRefreshToken(newRefreshTokenStr);
        } else {
            RefreshToken newToken = new RefreshToken(user, newRefreshTokenStr);
            refreshTokenRepository.save(newToken);
        }
    }

}
