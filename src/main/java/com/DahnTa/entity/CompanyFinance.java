package com.DahnTa.entity;

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
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "COMPANY_FINANCE_TB")
@Getter
public class CompanyFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "stock_price", nullable = false)
    private double stockPrice;

    @Column(name = "daily", nullable = false)
    private double daily;

    @Column(name = "sales", nullable = false)
    private double sales;

    @Column(name = "operating_profit", nullable = false)
    private double operatingProfit;

    @Column(name = "operating_profit_margin", nullable = false)
    private double operatingProfitMargin;

    @Column(name = "net_profit", nullable = false)
    private double netProfit;

    @Column(name = "sales_growth_rate", nullable = false)
    private String salesGrowthRate;

    @Column(name = "net_profit_growth_rate", nullable = false)
    private double netProfitGrowthRate;

    @Column(name = "estimates", nullable = false)
    private double estimates;

    @Column(name = "performance", nullable = false)
    private double performance;

    @Column(name = "consensus", nullable = false)
    private String consensus;

    @Column(name = "surprise", nullable = false)
    private double surprise;

    @Column(name = "buy_back_sares", nullable = false)
    private double buyBackSares;

    @Column(name = "dividend_payment", nullable = false)
    private double dividendPayment;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected CompanyFinance() {
    }

    private CompanyFinance(Stock stock, LocalDate date, double stockPrice, double daily, double sales,
        double operatingProfit, double operatingProfitMargin, double netProfit, String salesGrowthRate,
        double netProfitGrowthRate, double estimates, double performance, String consensus, double surprise,
        double buyBackSares, double dividendPayment, Long userId) {
        this.stock = stock;
        this.date = date;
        this.stockPrice = stockPrice;
        this.daily = daily;
        this.sales = sales;
        this.operatingProfit = operatingProfit;
        this.operatingProfitMargin = operatingProfitMargin;
        this.netProfit = netProfit;
        this.salesGrowthRate = salesGrowthRate;
        this.netProfitGrowthRate = netProfitGrowthRate;
        this.estimates = estimates;
        this.performance = performance;
        this.consensus = consensus;
        this.surprise = surprise;
        this.buyBackSares = buyBackSares;
        this.dividendPayment = dividendPayment;
        this.userId = userId;
    }

    @Builder
    public static CompanyFinance create(Stock stock, LocalDate date, double stockPrice, double daily,
        double sales, double operatingProfit, double operatingProfitMargin, double netProfit,
        String salesGrowthRate, double netProfitGrowthRate, double estimates, double performance,
        String consensus, double surprise, double buyBackSares, double dividendPayment, Long userId) {

        return CompanyFinance.builder()
            .stock(stock)
            .date(date)
            .stockPrice(stockPrice)
            .daily(daily)
            .sales(sales)
            .operatingProfit(operatingProfit)
            .operatingProfitMargin(operatingProfitMargin)
            .netProfit(netProfit)
            .salesGrowthRate(salesGrowthRate)
            .netProfitGrowthRate(netProfitGrowthRate)
            .estimates(estimates)
            .performance(performance)
            .consensus(consensus)
            .surprise(surprise)
            .buyBackSares(buyBackSares)
            .dividendPayment(dividendPayment)
            .userId(userId)
            .build();
    }
}
