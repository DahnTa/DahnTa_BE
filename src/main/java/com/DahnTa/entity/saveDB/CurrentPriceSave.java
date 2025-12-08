package com.DahnTa.entity.saveDB;

import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.exception.StockException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "current_price_save_db")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPriceSave {

    public static final double PERCENT = 100.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "market_price", nullable = false)
    private double marketPrice;

    public double calculateChangeRate(CurrentPriceSave previous) {

        return (this.currentPrice - previous.currentPrice) / previous.currentPrice * PERCENT;
    }

    public double calculateChangeAmount(CurrentPriceSave previous) {

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
