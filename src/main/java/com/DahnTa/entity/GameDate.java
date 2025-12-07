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
@Table(name = "game_date_tb")
@Getter
public class GameDate {

    public static final int DAY_INCREMENT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "`day`", nullable = false)
    private int day;

    protected GameDate() {}

    private GameDate(User user, LocalDate startDate, LocalDate endDate, int day) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.day = day;
    }

    public static GameDate create(User user, LocalDate startDate, LocalDate endDate, int day) {

        return new GameDate(user, startDate, endDate, day);
    }

    public void updateDay() {
        this.day += DAY_INCREMENT;
    }
}
