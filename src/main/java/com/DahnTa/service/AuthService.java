package com.DahnTa.service;

import com.DahnTa.Mapper.UserMapper;
import com.DahnTa.dto.request.LoginRequestDTO;
import com.DahnTa.dto.request.PasswordRequestDTO;
import com.DahnTa.dto.response.LoginResponseDTO;
import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.entity.User;
import com.DahnTa.jwt.JwtTokenProvider;
import com.DahnTa.repository.AuthRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, UserMapper userMapper,
        JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService,
        PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public void signupUser(SignUpRequestDTO signUpRequestDTO) {
        if (authRepository.existsByUserAccount(signUpRequestDTO.getUserAccount())) {
            throw new RuntimeException("이미 존재하는 계정입니다.");
        }
        User userEntity = userMapper.toEntity(signUpRequestDTO);
        userEntity.encodePassword(passwordEncoder);
        authRepository.save(userEntity);
    }


    public LoginResponseDTO authenticateToken(LoginRequestDTO loginRequestDTO) {
        User user = authRepository.findByUserAccount(loginRequestDTO.getUserAccount())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequestDTO.getUserPassword(), user.getUserPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserAccount());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        refreshTokenService.saveRefreshToken(user, refreshToken);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public void changePassword(User user, PasswordRequestDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.updatePassword(encodedPassword);
    }
}
