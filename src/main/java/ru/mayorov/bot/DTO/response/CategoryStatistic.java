package ru.mayorov.bot.DTO.response;

import ru.mayorov.bot.DTO.SpendingCategory;

public interface CategoryStatistic {
    String getMonth();
    SpendingCategory getCategory();
    Double getAmount();
}
