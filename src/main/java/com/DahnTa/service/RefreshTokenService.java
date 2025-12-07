package com.DahnTa.service;

import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.RefreshToken;
import com.DahnTa.entity.User;
import com.DahnTa.exception.RefreshTokenException;
import com.DahnTa.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void saveRefreshToken(User user, String token) {
        refreshTokenRepository.findByUser(user)
            .ifPresentOrElse(
                rt -> rt.updateRefreshToken(token),
                () -> refreshTokenRepository.save(new RefreshToken(user, token))
            );
    }

    public String getToken(User user) {
        return refreshTokenRepository.findByUser(user)
            .map(RefreshToken::getRefreshToken)
            .orElseThrow(() -> new RefreshTokenException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }
}

