package ru.mayorov.bot.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mayorov.bot.DTO.SpendingCategory;

@Getter
@AllArgsConstructor
public class StatisticsResponseByCategory {

    private double sum;
    private SpendingCategory category;

}
