package ru.ism.module;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class Transaction {
    private int id;
    private int userId;
    private Operation operation;
    private int sum;
    private String category;
    private String description;
    private LocalDate date;

    public Transaction(Operation operation, int sum, String category, String description, LocalDate date) {
        this.operation = operation;
        this.sum = sum;
        this.category = category;
        this.description = description;
        this.date = date;
    }

}
