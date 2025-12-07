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
import lombok.Getter;

@Entity
@Table(name = "company_finance_tb")
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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected CompanyFinance() {
    }

    private CompanyFinance(Stock stock, LocalDate date, String content, Long userId) {
        this.stock = stock;
        this.date = date;
        this.content = content;
        this.userId = userId;
    }

    public static CompanyFinance create(Stock stock, LocalDate date, String content, Long userId) {

        return new CompanyFinance(stock, date, content, userId);
    }
}
