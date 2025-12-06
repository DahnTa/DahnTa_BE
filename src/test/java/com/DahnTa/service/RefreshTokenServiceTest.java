package com.DahnTa.service;

import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.RefreshToken;
import com.DahnTa.entity.User;
import com.DahnTa.exception.RefreshTokenException;
import com.DahnTa.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);
    }

    @Test
    @DisplayName("리프레시 토큰 저장 - 새로운 토큰")
    void saveRefreshToken_SaveNewToken() {
        User user = new User("testUser", "password", "nick", 0, null);
        user.setId(1L);
        String newToken = "newRefreshToken";

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());

        refreshTokenService.saveRefreshToken(user, newToken);

        verify(refreshTokenRepository).save(argThat(rt ->
            rt.getUser().equals(user) &&
                rt.getRefreshToken().equals(newToken)
        ));
    }

    @Test
    @DisplayName("리프레시 토큰 저장 - 기존 토큰 업데이트")
    void saveRefreshToken_UpdateExistingToken() {
        User user = new User("testUser", "password", "nick", 0, null);
        user.setId(1L);
        RefreshToken existingToken = new RefreshToken(user, "oldToken");
        String newToken = "newRefreshToken";

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(existingToken));

        refreshTokenService.saveRefreshToken(user, newToken);

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("리프레시 토큰 조회 성공")
    void getToken_Success() {
        User user = new User("testUser", "password", "nick", 0, null);
        user.setId(1L);
        RefreshToken refreshToken = new RefreshToken(user, "validToken");

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));

        String token = refreshTokenService.getToken(user);

        assertThat(token).isEqualTo("validToken");
    }

    @Test
    @DisplayName("리프레시 토큰 조회 실패 - 토큰 없음")
    void getToken_Fail_TokenNotFound() {
        User user = new User("testUser", "password", "nick", 0, null);
        user.setId(1L);

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.getToken(user))
            .isInstanceOf(RefreshTokenException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}