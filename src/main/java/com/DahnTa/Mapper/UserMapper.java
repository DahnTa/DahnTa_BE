package com.DahnTa.Mapper;

import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static final int INITIAL_FUNDS = 10000;

    public User toEntity(SignUpRequestDTO signUpRequestDTO) {

        return User.builder()
            .userAccount(signUpRequestDTO.getUserAccount())
            .userPassword(signUpRequestDTO.getUserPassword())
            .userNickName(signUpRequestDTO.getUserNickName())
            .userCredit(INITIAL_FUNDS)
            .userProfileImageUrl(signUpRequestDTO.getUserProfileImageUrl())
            .build();

    }

}
