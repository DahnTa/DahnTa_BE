package com.DahnTa.entity.saveDB;

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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news_save_db")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsSave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;
}
