package ru.mayorov.repository;

import ru.mayorov.bot.DTO.response.CategoryStatistic;
import ru.mayorov.bot.DTO.response.MonthlyStatistic;
import ru.mayorov.bot.DTO.response.StatisticsResponseByCategory;
import ru.mayorov.model.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure,Long> {

    @Query("Select new ru.mayorov.bot.DTO.response.StatisticsResponseByCategory(" +
            " SUM(e.expend), e.category) " +
            " From Expenditure e WHERE e.userId = :userId group by e.category order by SUM(e.expend) desc")
    List<StatisticsResponseByCategory> getAllStatsByCategory(@Param("userId") long userId);

    @Query("SELECT MONTHNAME(e.datetime) as month, SUM(e.expend) as total " +
            "FROM Expenditure e " +
            "WHERE e.userId = :userId AND YEAR(e.datetime) = :year " +
            "GROUP BY MONTHNAME(e.datetime), MONTH(e.datetime) " +
            "ORDER BY MONTH(e.datetime) ASC ")
    List<MonthlyStatistic> findMonthlyStats(@Param("userId") long userId, @Param("year") int currentYear);

    @Query("SELECT MONTHNAME(e.datetime) as month, e.category as category, SUM(e.expend) as amount " +
            "FROM Expenditure e " +
            "WHERE e.userId = :userId AND YEAR(e.datetime) = :year " +
            "GROUP BY MONTHNAME(e.datetime), e.category " +
            "ORDER BY SUM(e.expend) desc")
    List<CategoryStatistic> findCategoryStats(@Param("userId") long userId, @Param("year") int currentYear);

}
