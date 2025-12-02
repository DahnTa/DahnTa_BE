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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected News() {
    }

    private News(Stock stock, LocalDate date, String title, String content, String keyword, Long userId) {
        this.stock = stock;
        this.date = date;
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.userId = userId;
    }

    @Builder
    public static News create(Stock stock, LocalDate date, String title, String content, String keyword,
        Long userId) {

        return News.builder()
            .stock(stock)
            .date(date)
            .title(title)
            .content(content)
            .keyword(keyword)
            .userId(userId)
            .build();
    }
}
