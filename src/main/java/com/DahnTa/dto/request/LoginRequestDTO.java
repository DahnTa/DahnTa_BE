package com.DahnTa.dto.request;

public class LoginRequestDTO {
    private Long id;
    private String userAccount;
    private String userPassword;


    // 기본 생성자
    public LoginRequestDTO(String userAccount, String userPassword) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
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
