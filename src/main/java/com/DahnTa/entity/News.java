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
@Table(name = "NEWS_TB")
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "disclaimer", nullable = false)
    private String disclaimer;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected News() {
    }

    private News(Stock stock, LocalDate date, String disclaimer, String content, Long userId) {
        this.stock = stock;
        this.date = date;
        this.disclaimer = disclaimer;
        this.content = content;
        this.userId = userId;
    }

    @Builder
    public static News create(Stock stock, LocalDate date, String disclaimer, String content,
        Long userId) {

        return News.builder()
            .stock(stock)
            .date(date)
            .disclaimer(disclaimer)
            .content(content)
            .userId(userId)
            .build();
    }
}
