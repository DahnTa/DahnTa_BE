package com.DahnTa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    public void updatePassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int calculateFinalAmount(int totalReturnRate) {
        return this.userCredit + totalReturnRate;
    }

    public double calculateReturnRate(int finalAmount, int initialFunds) {
        return ((double) finalAmount - initialFunds) / initialFunds * 100.0;
    }
}
