package ru.mayorov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mayorov.bot.DTO.ExpenseCounterDTO;
import ru.mayorov.bot.DTO.SpendingCategory;
import ru.mayorov.bot.DTO.response.CategoryStatistic;
import ru.mayorov.bot.DTO.response.MonthlyStatistic;
import ru.mayorov.bot.DTO.response.StatisticsResponseByCategory;
import ru.mayorov.model.Expenditure;
import ru.mayorov.repository.ExpenditureRepository;
import ru.mayorov.service.BotService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BotServiceTest {
    @Mock
    private ExpenditureRepository repository;

    @InjectMocks
    private BotService service;

    private ExpenseCounterDTO expenseCounterDTO;

    @BeforeEach
    void setUp(){
        expenseCounterDTO = new ExpenseCounterDTO();
        expenseCounterDTO.setCategory("SPORT");
        expenseCounterDTO.setDate(LocalDate.now());
        expenseCounterDTO.setExpend(1200D);
        expenseCounterDTO.setUserUID(123L);

    }

    @Test
    void addExpenseTest(){
        Expenditure expectedExpenditure = new Expenditure();
        expectedExpenditure.setUserId(expenseCounterDTO.getUserUID());
        expectedExpenditure.setExpend(expenseCounterDTO.getExpend());
        expectedExpenditure.setCategory(SpendingCategory.valueOf(expenseCounterDTO.getCategory()));

        Mockito.when(repository.save(Mockito.any())).thenReturn(expectedExpenditure);

        Expenditure actualExpenditure  = service.addExpense(expenseCounterDTO);

        verify(repository).save(any());
        assertNotNull(actualExpenditure);
        assertEquals(expectedExpenditure.getUserId(), actualExpenditure.getUserId());
        assertEquals(expectedExpenditure.getExpend(), actualExpenditure.getExpend());
        assertEquals(expectedExpenditure.getCategory(), actualExpenditure.getCategory());
    }

    @Test
    void getAllStatisticsByCategoryTest(){
        Mockito.when(repository.getAllStatsByCategory(
                Mockito.anyLong())).thenReturn(List.of(new StatisticsResponseByCategory(100.0, SpendingCategory.FOOD)));
        String result = service.getAllStatisticsByCategory(123L);
        assertTrue(result.contains("продукты"));
    }

    @Test
    void getStatisticsByCurrentYearTest(){
        Mockito.when(repository.findMonthlyStats(Mockito.anyLong(),Mockito.anyInt()))
                .thenReturn(List.of(new MonthlyStatistic() {
                    @Override
                    public String getMonth() {
                        return "March";
                    }

                    @Override
                    public Double getTotal() {
                        return 100.0;
                    }
                }));

        Mockito.when(repository.findCategoryStats(Mockito.anyLong(),Mockito.anyInt()))
                .thenReturn(List.of(new CategoryStatistic() {
                    @Override
                    public String getMonth() {
                        return "March";
                    }

                    @Override
                    public SpendingCategory getCategory() {
                        return SpendingCategory.FOOD;
                    }

                    @Override
                    public Double getAmount() {
                        return 120.0;
                    }
                }));

        String result = service.getStatisticsByCurrentYear(123L,2023);

        assertTrue(result.contains("продукты"));
        assertTrue(result.contains("March"));
        assertTrue(result.contains("120,00 ₽"));
        assertTrue(result.contains("100,00 ₽"));
    }
}
