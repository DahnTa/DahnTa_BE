package com.DahnTa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userAccount", nullable = false)
    private String userAccount;

    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @Column(name = "userCredit", nullable = false)
    private int userCredit;

    @Column(name = "userNickName", nullable = true)
    private String userNickName;

    @Column(name = "userProfileImageUrl", nullable = true)
    private String userProfileImageUrl;


    public User(String userAccount, String userPassword,
        String userNickName, int userCredit, String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
        this.userNickName = userNickName;
        this.userCredit = userCredit;
        this.userPassword = userPassword;
        this.userAccount = userAccount;
    }


    public void updatePassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void validatePassword(String inputPassword) {
        if (!this.userPassword.equals(inputPassword)) {
            throw new RuntimeException("Password incorrect");
        }
    }
}
