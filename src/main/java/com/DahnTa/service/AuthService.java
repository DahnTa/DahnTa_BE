package com.DahnTa.service;

import com.DahnTa.dto.AuthDTO;
import com.DahnTa.entity.Auth;
import com.DahnTa.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private static AuthRepository authRepository;

    public static void signupUser(AuthDTO authDTO) {
         Auth authEntity = toEntity(authDTO);
         authRepository.save(authEntity);
    }


    public static void authenticateToken(AuthDTO authDTO) {

    }

    // dto to entity
    private static Auth toEntity(AuthDTO authDTO) {
         Auth authEntity = new Auth(authDTO.getId(), authDTO.getUserAccount(), authDTO.getUserPassword(),
             authDTO.getUserNickName(), authDTO.getUserCredit(), authDTO.getUserProfileImageUrl());
        return authEntity;
    }

}
