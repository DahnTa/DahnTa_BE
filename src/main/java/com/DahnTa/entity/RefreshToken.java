package com.DahnTa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REFRESH_TOKEN_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   // 1:1 관계
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "refreshToken", nullable = false)
    private String refreshToken;

    public RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    //@todo : getter 어노테이션
    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }


    public String getRefreshToken() {
        return refreshToken;
    }


    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }


}
