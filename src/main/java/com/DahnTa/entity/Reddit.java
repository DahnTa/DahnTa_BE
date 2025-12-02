package com.DahnTa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "REDDIT_TB")
@Getter
public class Reddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected Reddit() {}

    private Reddit(Stock stock, Long userId) {
        this.stock = stock;
        this.userId = userId;
    }

    @Builder
    public static Reddit create(Stock stock, Long userId) {

        return Reddit.builder()
            .stock(stock)
            .userId(userId)
            .build();
    }
}
