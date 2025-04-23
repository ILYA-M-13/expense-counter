package org.example.repository;

import org.example.model.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure,Long> {

    List<Expenditure> findExpenditureByUserId(long userId);

    @Query("SELECT SUM(e.expend) FROM Expenditure e WHERE e.userId = :userId")
    Optional<Double> findSumExpendByUserId(@Param("userId") long userId);
}
