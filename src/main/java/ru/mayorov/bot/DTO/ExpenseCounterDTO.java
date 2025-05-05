package ru.mayorov.bot.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ExpenseCounterDTO {
    private String category;
    private Double expend;
    private LocalDate date;
    private Long userUID;

}
