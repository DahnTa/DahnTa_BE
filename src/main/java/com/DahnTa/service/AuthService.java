package com.DahnTa.service;

import com.DahnTa.Mapper.UserMapper;
import com.DahnTa.dto.request.LoginRequestDTO;
import com.DahnTa.dto.request.PasswordRequestDTO;
import com.DahnTa.dto.response.LoginResponseDTO;
import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.User;
import com.DahnTa.exception.AuthException;
import com.DahnTa.jwt.JwtTokenProvider;
import com.DahnTa.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserMapper userMapper,
        JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public void signupUser(SignUpRequestDTO signUpRequestDTO) {
        if (userRepository.existsByUserAccount(signUpRequestDTO.getUserAccount())) {
            throw new AuthException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        User userEntity = userMapper.toEntity(signUpRequestDTO);
        userEntity.encodePassword(passwordEncoder);
        userRepository.save(userEntity);
    }


    public LoginResponseDTO authenticateToken(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUserAccount(loginRequestDTO.getUserAccount())
            .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequestDTO.getUserPassword(), user.getUserPassword())) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
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
