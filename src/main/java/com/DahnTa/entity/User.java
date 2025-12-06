package com.DahnTa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "USER_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_account", nullable = false)
    private String userAccount;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_credit", nullable = false)
    private double userCredit;

    @Column(name = "user_nickname", nullable = true)
    private String userNickName;

    @Column(name = "user_profile_image_url", nullable = true)
    private String userProfileImageUrl;

    @Builder
    public User(String userAccount, String userPassword,
        String userNickName, double userCredit, String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
        this.userNickName = userNickName;
        this.userCredit = userCredit;
        this.userPassword = userPassword;
        this.userAccount = userAccount;
    }


    public void updatePassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void increaseCredit(double amount) {
        this.userCredit += amount;
    }

    public void deductCredit(double amount) {
        this.userCredit -= amount;
    }

    public double calculateFinalAmount(double totalReturnRate) {
        return this.userCredit + totalReturnRate;
    }

    public double calculateReturnRate(double finalAmount, double initialFunds) {
        return ((double) finalAmount - initialFunds) / initialFunds * 100.0;

    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.userPassword = passwordEncoder.encode(this.userPassword);
    }
}
