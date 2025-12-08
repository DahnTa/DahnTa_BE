package com.DahnTa.entity;

import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.exception.StockException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "current_price_tb")
@Getter
@AllArgsConstructor
public class CurrentPrice {

    public static final double PERCENT = 100.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "market_price", nullable = false)
    private double marketPrice;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected CurrentPrice() {
    }

    private CurrentPrice(Stock stock, LocalDate date, double currentPrice, double marketPrice, Long userId) {
        this.stock = stock;
        this.date = date;
        this.currentPrice = currentPrice;
        this.marketPrice = marketPrice;
        this.userId = userId;
    }

    public static CurrentPrice create(Stock stock, LocalDate date, double currentPrice, double marketPrice,
        Long userId) {

        return new CurrentPrice(stock, date, currentPrice, marketPrice, userId);
    }

    public double calculateChangeRate(CurrentPrice previous) {

        return (this.currentPrice - previous.currentPrice) / previous.currentPrice * PERCENT;
    }

    public double calculateChangeAmount(CurrentPrice previous) {

        return this.currentPrice - previous.currentPrice;
    }

    public double calculateAvailableOrderAmount(double userCredit) {

        return userCredit / this.currentPrice * this.currentPrice;
    }

    public void validateBuyQuantity(double userCredit, double buyQuantity) {
        if (userCredit < this.currentPrice * buyQuantity) {
            throw new StockException(ErrorCode.INSUFFICIENT_CREDIT);
        }
    }
}
