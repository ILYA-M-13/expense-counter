package ru.mayorov.model;


import jakarta.persistence.*;
import lombok.*;
import ru.mayorov.bot.DTO.SpendingCategory;

import java.util.Date;


@Entity
@Data
@Table(name = "expenditure", indexes = @Index(name = "idx_expenditure_user_datetime",
        columnList = "user_id, datetime"))
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id",nullable = false)
    private long userId;

    @Column(nullable = false)
    private double expend;

    @Enumerated(EnumType.STRING)
    private SpendingCategory category;

    @Column(columnDefinition = "TIMESTAMP",nullable = false)
    private Date datetime;

}
