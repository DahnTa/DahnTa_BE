package com.DahnTa.dto;

public class AuthDTO {
    private Long id;
    private String userAccount;
    private String userPassword;
    private int userCredit;
    private String userNickName;
    private String userProfileImageUrl;

    // ID & paawd 변경 생성자
    public AuthDTO(String userPassword, String userAccount) {
        this.userPassword = userPassword;
        this.userAccount = userAccount;
    }

    // 기본 생성자
    public AuthDTO(Long id, String userAccount, String userPassword, int userCredit,
        String userNickName, String userProfileImageUrl) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userCredit = userCredit;
        this.userNickName = userNickName;
        this.userProfileImageUrl = userProfileImageUrl;
    }


    // setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserCredit(int userCredit) {
        this.userCredit = userCredit;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }



    // getter
    public Long getId() {
        return id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUserCredit() {
        return userCredit;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }



}
