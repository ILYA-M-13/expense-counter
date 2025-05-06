package ru.mayorov.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.mayorov.bot.DTO.response.CategoryStatistic;
import ru.mayorov.bot.DTO.response.ExpResponse;
import ru.mayorov.bot.DTO.response.MonthlyStatistic;
import ru.mayorov.bot.DTO.response.StatisticsResponseByCategory;
import ru.mayorov.model.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure,Long> {

    @Query("Select new ru.mayorov.bot.DTO.response.StatisticsResponseByCategory(" +
            " SUM(e.expend), e.category) " +
            " From Expenditure e WHERE e.userId = :userId group by e.category order by SUM(e.expend) desc")
    List<StatisticsResponseByCategory> getAllStatsByCategory(@Param("userId") long userId);

    @Query("SELECT to_char(e.datetime, 'Month') as month, SUM(e.expend) as total " +
            "FROM Expenditure e " +
            "WHERE e.userId = :userId AND EXTRACT(YEAR FROM e.datetime) = :year " +
            "GROUP BY to_char(e.datetime, 'Month'), EXTRACT(MONTH FROM e.datetime) " +
            "ORDER BY EXTRACT(MONTH FROM e.datetime) ASC ")
    List<MonthlyStatistic> findMonthlyStats(@Param("userId") long userId, @Param("year") int currentYear);

    @Query("SELECT to_char(e.datetime, 'Month') as month, e.category as category, SUM(e.expend) as amount " +
            "FROM Expenditure e " +
            "WHERE e.userId = :userId AND EXTRACT(YEAR FROM e.datetime) = :year " +
            "GROUP BY to_char(e.datetime, 'Month'), e.category " +
            "ORDER BY SUM(e.expend) desc")
    List<CategoryStatistic> findCategoryStats(@Param("userId") long userId, @Param("year") int currentYear);

    @Query("SELECT new ru.mayorov.bot.DTO.response.ExpResponse(" +
            " e.category, e.expend, to_char(e.datetime, 'yyyy-MM-dd HH:mm:ss') ) " +
            "FROM Expenditure e " +
            "WHERE e.userId = :userId " +
            "ORDER BY e.datetime DESC LIMIT 1")
    Optional<ExpResponse> getLastExp(long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Expenditure e WHERE e.id = " +
            "(SELECT e2.id FROM Expenditure e2 WHERE e2.userId = :userId " +
            "ORDER BY e2.datetime DESC LIMIT 1)")
    int deleteLastExpenditure(long userId);
}
