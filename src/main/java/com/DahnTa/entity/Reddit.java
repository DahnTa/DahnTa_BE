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
@Table(name = "REDDIT_TB")
@Getter
public class Reddit {

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

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "num_comment", nullable = false)
    private int numComment;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected Reddit() {
    }

    private Reddit(Stock stock, LocalDate date, String content, int score, int numComment, Long userId) {
        this.stock = stock;
        this.date = date;
        this.content = content;
        this.score = score;
        this.numComment = numComment;
        this.userId = userId;
    }

    @Builder
    public static Reddit create(Stock stock, LocalDate date, String content, int score, int numComment,
        Long userId) {

        return Reddit.builder()
            .stock(stock)
            .date(date)
            .content(content)
            .score(score)
            .numComment(numComment)
            .userId(userId)
            .build();
    }
}
