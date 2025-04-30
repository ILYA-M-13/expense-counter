package ru.mayorov.model;


import jakarta.persistence.*;
import ru.mayorov.bot.DTO.SpendingCategory;

import java.util.Date;


@Entity
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

    public Expenditure() {
    }

    public Expenditure(int id, long userId, double expend, SpendingCategory category, Date time) {
        this.id = id;
        this.userId = userId;
        this.expend = expend;
        this.category = category;

        this.datetime = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getExpend() {
        return expend;
    }

    public void setExpend(double expend) {
        this.expend = expend;
    }

    public SpendingCategory getCategory() {
        return category;
    }

    public void setCategory(SpendingCategory category) {
        this.category = category;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
