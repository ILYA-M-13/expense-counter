package ru.mayorov.bot.DTO;

import java.time.LocalDate;

public class ExpenseCounterDTO {
    private String category;
    private Double expend;
    private LocalDate date;
    private String comment;
    private Long userUID;

    public Long getUserUID() {
        return userUID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getExpend() {
        return expend;
    }

    public void setExpend(Double expend) {
        this.expend = expend;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserUID(Long userUID) {
        this.userUID = userUID;
    }
}
