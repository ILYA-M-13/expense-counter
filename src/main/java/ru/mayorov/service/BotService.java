package ru.mayorov.service;


import ru.mayorov.bot.DTO.ExpenseCounterDTO;
import ru.mayorov.bot.DTO.SpendingCategory;
import ru.mayorov.bot.DTO.response.*;

import ru.mayorov.model.Expenditure;
import ru.mayorov.repository.ExpenditureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BotService {
    private final ExpenditureRepository repository;

    public BotService(ExpenditureRepository repository) {
        this.repository = repository;
    }


    public Expenditure addExpense(ExpenseCounterDTO expenseCounterDTO) {

        Expenditure expenditure = new Expenditure();
        expenditure.setCategory(SpendingCategory.valueOf(expenseCounterDTO.getCategory()));
        expenditure.setExpend(expenseCounterDTO.getExpend());
        expenditure.setUserId(expenseCounterDTO.getUserUID());

        expenditure.setDatetime(java.sql.Date.valueOf(expenseCounterDTO.getDate()));
        return repository.save(expenditure);

    }

    public String getAllStatisticsByCategory(long userId) {
        return "\uD83C\uDFC6Категории трат за все время\uD83C\uDFC6\r\n" +
                repository.getAllStatsByCategory(userId).stream()
                        .map(stat -> " → " + stat.getCategory().getName() + ":  " + stat.getSum() + " ₽")
                        .collect(Collectors.joining("\r\n"));
    }


    public String getStatisticsByCurrentYear(long userId, int currentYear) {
        Map<String, StatisticsResponseByMonth> result = new LinkedHashMap<>();

        for (MonthlyStatistic monthlyStatistic : repository.findMonthlyStats(userId,currentYear)) {
            result.put(monthlyStatistic.getMonth(),
                    new StatisticsResponseByMonth(monthlyStatistic.getMonth(),
                            monthlyStatistic.getTotal(),
                            new ArrayList<>()
                    ));
        }
        for (CategoryStatistic categoryStatistic : repository.findCategoryStats(userId,currentYear)) {
            StatisticsResponseByMonth response = result.get(categoryStatistic.getMonth());
            response.getCategoryList().add(
                    new StatisticsResponseByCategory(categoryStatistic.getAmount(),categoryStatistic.getCategory()));

        }

        return "\uD83D\uDCB0 Отчет по месяцам за " + currentYear + " год \uD83D\uDCB0\n\n" +
                result.values().stream()
                        .map(month -> {
                            String categories = month.getCategoryList().stream()
                                    .map(cat -> String.format("%s: %.2f ₽", cat.getCategory().getName(), cat.getSum()))
                                    .collect(Collectors.joining("\n   → "));

                            return String.format(
                                    "• %s: %.2f ₽\n   → %s",
                                    month.getMonth(),
                                    month.getSpentPerMonth(),
                                    categories
                            );
                        })
                        .collect(Collectors.joining("\n\n"));

        }

}
