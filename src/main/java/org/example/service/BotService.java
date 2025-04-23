package org.example.service;


import org.example.bot.DTO.ExpenseCounterDTO;
import org.example.bot.DTO.SpendingCategory;
import org.example.model.Expenditure;
import org.example.repository.ExpenditureRepository;
import org.springframework.stereotype.Service;

@Service
public class BotService {
private final ExpenditureRepository repository;

    public BotService(ExpenditureRepository repository) {
        this.repository = repository;
    }


    public Expenditure addExpense(ExpenseCounterDTO expenseCounterDTO){

        Expenditure expenditure = new Expenditure();
        expenditure.setCategory(SpendingCategory.valueOf(expenseCounterDTO.getCategory()));
        expenditure.setExpend(expenseCounterDTO.getExpend());
        expenditure.setUserId(expenseCounterDTO.getUserUID());
        expenditure.setNote(expenseCounterDTO.getComment());
        expenditure.setDatetime(java.sql.Date.valueOf(expenseCounterDTO.getDate()));
        return repository.save(expenditure);

    }
}
