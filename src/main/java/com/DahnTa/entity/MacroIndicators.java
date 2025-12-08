package com.DahnTa.entity;

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
import lombok.Setter;

@Entity
@Table(name = "macro_indicators_tb")
@Getter
@AllArgsConstructor
public class MacroIndicators {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected MacroIndicators() {
    }

    private MacroIndicators(LocalDate date, String content, Long userId) {
        this.date = date;
        this.content = content;
        this.userId = userId;
    }

    public static MacroIndicators create(LocalDate date, String content, Long userId) {

        return new MacroIndicators(date, content, userId);
    }
}
