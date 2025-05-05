package ru.mayorov.bot.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatisticsResponseByMonth {
  private String month;
  private double spentPerMonth;
  private List<StatisticsResponseByCategory> categoryList;

}
