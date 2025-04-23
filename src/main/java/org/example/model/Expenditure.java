package org.example.model;


import jakarta.persistence.*;
import org.example.bot.DTO.SpendingCategory;

import java.util.Date;


@Entity
@Table(name = "expenditure")
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

    @Column(columnDefinition = "LONGTEXT")
    private String note;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date datetime;

    public Expenditure() {
    }

    public Expenditure(int id, long userId, double expend, SpendingCategory category, String note, Date time) {
        this.id = id;
        this.userId = userId;
        this.expend = expend;
        this.category = category;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
