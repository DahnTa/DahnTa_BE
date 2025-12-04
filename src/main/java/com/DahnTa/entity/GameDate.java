package com.DahnTa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "GAME_DATE_TB")
@Getter
public class GameDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
     */

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "day", nullable = false)
    private int day;

    protected GameDate() {}

    @Builder
    private GameDate(User user, LocalDate startDate, LocalDate endDate, int day) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.day = day;
    }

    public static GameDate create(User user, LocalDate startDate, LocalDate endDate, int day) {

        return GameDate.builder()
            .user(user)
            .startDate(startDate)
            .endDate(endDate)
            .day(day)
            .build();
    }

    public void updateDay() {
        this.day += 1;
    }
}
