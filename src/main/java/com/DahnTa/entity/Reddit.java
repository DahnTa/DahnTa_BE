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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reddit_tb")
@Getter
@AllArgsConstructor
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "num_comment", nullable = false)
    private int numComment;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected Reddit() {
    }

    private Reddit(Stock stock, LocalDate date, String title, String content, int score, int numComment, Long userId) {
        this.stock = stock;
        this.date = date;
        this.title = title;
        this.content = content;
        this.score = score;
        this.numComment = numComment;
        this.userId = userId;
    }

    public static Reddit create(Stock stock, LocalDate date, String title, String content, int score, int numComment,
        Long userId) {

        return new Reddit(stock, date, title, content, score, numComment, userId);
    }
}
