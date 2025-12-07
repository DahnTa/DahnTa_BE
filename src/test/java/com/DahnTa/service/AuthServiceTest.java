package com.DahnTa.service;

import com.DahnTa.Mapper.UserMapper;
import com.DahnTa.dto.request.LoginRequestDTO;
import com.DahnTa.dto.request.PasswordRequestDTO;
import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.dto.response.LoginResponseDTO;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.User;
import com.DahnTa.exception.AuthException;
import com.DahnTa.jwt.JwtTokenProvider;
import com.DahnTa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, userMapper, jwtTokenProvider,
            refreshTokenService, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 계정")
    void signupUser_Fail_AccountAlreadyExists() {
        SignUpRequestDTO dto = new SignUpRequestDTO(1L, "test", "pass", "nick", "url");
        when(userRepository.existsByUserAccount("test")).thenReturn(true);

        assertThatThrownBy(() -> authService.signupUser(dto))
            .isInstanceOf(AuthException.class)
            .satisfies(exception -> {
                AuthException authEx = (AuthException) exception;
                assertThat(authEx.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_ALREADY_EXISTS);
            });

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupUser_Success() {
        SignUpRequestDTO dto = new SignUpRequestDTO(1L, "newUser", "pass", "nick", "url");
        User userEntity = new User("newUser", "pass", "nick", 0, null);

        when(userRepository.existsByUserAccount("newUser")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        authService.signupUser(dto);

        verify(userRepository).save(argThat(user ->
            user.getUserAccount().equals("newUser")
        ));
        verify(passwordEncoder).encode("pass");
    }

    @Test
    @DisplayName("로그인 성공")
    void authenticateToken_Success() {
        LoginRequestDTO dto = new LoginRequestDTO("existingUser", "pass");
        User user = new User("existingUser", "encodedPass", "nick", 0, null);
        user.setId(1L);

        when(userRepository.findByUserAccount("existingUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(user.getId(), "existingUser")).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(user.getId())).thenReturn("refreshToken");

        LoginResponseDTO result = authService.authenticateToken(dto);

        assertThat(result.accessToken()).isEqualTo("accessToken");
        assertThat(result.refreshToken()).isEqualTo("refreshToken");
        verify(refreshTokenService).saveRefreshToken(user, "refreshToken");
    }

    @Test
    @DisplayName("로그인 실패 - 계정 없음")
    void authenticateToken_Fail_UserNotFound() {
        LoginRequestDTO dto = new LoginRequestDTO("notExistUser", "pass");
        when(userRepository.findByUserAccount("notExistUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticateToken(dto))
            .isInstanceOf(AuthException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void authenticateToken_Fail_InvalidPassword() {
        LoginRequestDTO dto = new LoginRequestDTO("existingUser", "wrongPass");
        User user = new User("existingUser", "encodedPass", "nick", 0, null);

        when(userRepository.findByUserAccount("existingUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThatThrownBy(() -> authService.authenticateToken(dto))
            .isInstanceOf(AuthException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword_Success() {
        User user = new User("user", "oldPass", "nick", 0, null);
        user.setId(1L);
        PasswordRequestDTO dto = new PasswordRequestDTO();
        dto.setPassword("newPass");

        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        authService.changePassword(user, dto);

        verify(passwordEncoder).encode("newPass");
        assertThat(user.getUserPassword()).isEqualTo("encodedNewPass");
    }
}