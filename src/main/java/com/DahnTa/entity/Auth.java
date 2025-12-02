package com.DahnTa.entity;

import jakarta.persistence.Entity;

@Entity
public class Auth {
    private Long id;
    private String userAccount;
    private String userPassword;
    private int userCredit;
    private String userNickName;
    private String userProfileImageUrl;

    // 기본 생성자
    public Auth(Long id, String userAccount, String userPassword,
        String userNickName, int userCredit, String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
        this.userNickName = userNickName;
        this.userCredit = userCredit;
        this.userPassword = userPassword;
        this.userAccount = userAccount;
        this.id = id;
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
