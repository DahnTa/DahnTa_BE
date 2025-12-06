package com.DahnTa.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SignUpRequestDTO {
    private Long id;

    @NotBlank
    private String userAccount;

    @NotBlank
    private String userPassword;

//  해당 값은 임의로 저장할 수 없어야 함
//    private int userCredit;

    // @todo : 기본 닉과, 유저 프로필 필요
    private String userNickName;
    private String userProfileImageUrl;


    // 기본 생성자
    public SignUpRequestDTO(Long id, String userAccount, String userPassword,
        String userNickName, String userProfileImageUrl) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userNickName = userNickName;
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }




}
