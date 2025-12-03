package com.DahnTa.service;

import com.DahnTa.entity.RefreshToken;
import com.DahnTa.entity.User;
import com.DahnTa.repository.RefreshTokenRepository;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JWTService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Key key;

    public JWTService(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }


    // access token 생성 클래스
    public String generateAccessToken(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("userId", userId);


        return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 min
            .signWith(key)
            .compact();

    }


    // refreshToken 생성 메서드
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14)) // 14 day
            .signWith(key)
            .compact();
    }


//    // refresh token 만료 확인
//    public static Boolean validationToken(String refreshToken) {
//        // 만료 확인
//        try {
//            SecretKey secretKey = Keys.hmacShaKeyFor(key.getEncoded().getBytes(
//                StandardCharsets.UTF_8));
//            Jwts.parser()
//                .setSigningKey(secretKey)    // 비밀값으로 복호화
//                .build()
//                .parseClaimsJws(refreshToken);
//            return true;
//        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰이다.
//            return false;
//        }
//    }

//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            // ExpiredJwtException, MalformedJwtException 등 발생 시 false
//            return false;
//        }
//    }

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
