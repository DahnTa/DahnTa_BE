package com.DahnTa.service;

import com.DahnTa.dto.request.LoginRequestDTO;
import com.DahnTa.dto.request.PasswordRequestDTO;
import com.DahnTa.dto.response.LoginResponseDTO;
import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.entity.User;
import com.DahnTa.repository.AuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private AuthRepository authRepository;
    private JWTService jwtService;

    public AuthService(AuthRepository authRepository, JWTService jwtService) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
    }


    public void signupUser(SignUpRequestDTO signUpRequestDTO) {
         User userEntity = toEntity(signUpRequestDTO);
         authRepository.save(userEntity);
    }


    public LoginResponseDTO authenticateToken(LoginRequestDTO loginRequestDTO) {
        User user = authRepository.findByUserAccount(loginRequestDTO.getUserAccount())
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.validatePassword(loginRequestDTO.getUserPassword());

        String accessToken = jwtService.generateAccessToken(user.getUserAccount(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        jwtService.saveToken(user, refreshToken);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public void changePassword(String bearerToken, PasswordRequestDTO dto) {
        String token = bearerToken.replace("Bearer ", "");

        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        Long userId = jwtService.extractUserId(token);

        User user = authRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.updatePassword(dto.getPassword());
    }


    // dto to entity
    // @todo : builder pattern으로 개선 필요
    // @todo : password 암호화하여 저장 필요
    private User toEntity(SignUpRequestDTO signUpRequestDTO) {
         User userEntity = new User(signUpRequestDTO.getUserAccount(), signUpRequestDTO.getUserPassword(),
             signUpRequestDTO.getUserNickName(), 10000000, signUpRequestDTO.getUserProfileImageUrl());
        return userEntity;
    }

}
