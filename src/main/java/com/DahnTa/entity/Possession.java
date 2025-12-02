package com.DahnTa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "NEWS_TB")
@Getter
public class Possession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    /*
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
     */

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected Possession() {}

    private Possession(Stock stock, User user, int quantity) {
        this.stock = stock;
        this.user = user;
        this.quantity = quantity;
    }

    @Builder
    public static Possession create(Stock stock, User user, int quantity) {

        return Possession.builder()
            .stock(stock)
            .user(user)
            .quantity(quantity)
            .build();
    }
}
