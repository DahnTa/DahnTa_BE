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
@Table(name = "TOTAL_ANALYSIS_TB")
@Getter
public class TotalAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "analyze", nullable = false)
    private String analyze;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected TotalAnalysis() {
    }

    private TotalAnalysis(Stock stock, LocalDate date, String companyName, String analyze, Long userId) {
        this.stock = stock;
        this.date = date;
        this.companyName = companyName;
        this.analyze = analyze;
        this.userId = userId;
    }

    @Builder
    public static TotalAnalysis create(Stock stock, LocalDate date, String companyName, String analyze,
        Long userId) {

        return TotalAnalysis.builder()
            .stock(stock)
            .date(date)
            .companyName(companyName)
            .analyze(analyze)
            .userId(userId)
            .build();
    }
}
