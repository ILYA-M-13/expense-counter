package ru.mayorov.bot.DTO.response;

import ru.mayorov.bot.DTO.SpendingCategory;

public class StatisticsResponseByCategory {

    private double sum;
    private SpendingCategory category;

    public StatisticsResponseByCategory(Double sum, SpendingCategory category) {
        this.sum = sum;
        this.category = category;
    }

    public SpendingCategory getCategory() {
        return category;
    }

    public void setCategory(SpendingCategory category) {
        this.category = category;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

}
