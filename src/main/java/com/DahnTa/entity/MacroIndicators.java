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
@Table(name = "MACRO_INDICATORS_TB")
@Getter
public class MacroIndicators {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "disclaimer", nullable = false)
    private String disclaimer;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected MacroIndicators() {
    }

    private MacroIndicators(LocalDate date, String disclaimer, String content, Long userId) {
        this.date = date;
        this.disclaimer = disclaimer;
        this.content = content;
        this.userId = userId;
    }

    @Builder
    public static MacroIndicators create(LocalDate date, String disclaimer, String content,
        Long userId) {

        return MacroIndicators.builder()
            .date(date)
            .disclaimer(disclaimer)
            .content(content)
            .userId(userId)
            .build();
    }
}
