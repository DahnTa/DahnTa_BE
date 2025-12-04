package com.DahnTa.dto.Auth;

public class LoginRequestDTO {
    private Long id;
    private String userAccount;
    private String userPassword;


    // 기본 생성자
    public LoginRequestDTO(String userPassword, String userAccount) {
        this.userPassword = userPassword;
        this.userAccount = userAccount;
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



}
