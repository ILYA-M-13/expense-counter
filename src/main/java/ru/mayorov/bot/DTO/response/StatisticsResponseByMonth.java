package ru.mayorov.bot.DTO.response;

import java.util.List;

public class StatisticsResponseByMonth {
  private String month;
  private double spentPerMonth;
  private List<StatisticsResponseByCategory> categoryList;

    public StatisticsResponseByMonth(String month, double spentPerMonth, List<StatisticsResponseByCategory> categoryList) {
        this.month = month;
        this.spentPerMonth = spentPerMonth;
        this.categoryList = categoryList;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getSpentPerMonth() {
        return spentPerMonth;
    }

    public void setSpentPerMonth(double spentPerMonth) {
        this.spentPerMonth = spentPerMonth;
    }

    public List<StatisticsResponseByCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<StatisticsResponseByCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
