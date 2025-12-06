package com.DahnTa.Mapper;

import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(SignUpRequestDTO signUpRequestDTO) {

        return User.builder()
            .userAccount(signUpRequestDTO.getUserAccount())
            .userPassword(signUpRequestDTO.getUserPassword())
            .userNickName(signUpRequestDTO.getUserNickName())
            .userCredit(10000000)
            .userProfileImageUrl(signUpRequestDTO.getUserProfileImageUrl())
            .build();

    }

}
