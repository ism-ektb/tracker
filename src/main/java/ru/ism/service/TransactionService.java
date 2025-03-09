package ru.ism.service;

import ru.ism.module.Operation;
import ru.ism.module.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    void addTransaction(int userId, Transaction transaction);
    void removeTransaction(int userId, int id);
    List<Transaction> getTransactions(int userId);
    void updateTransaction(int userId, int id, Transaction transaction);
    List<Transaction> getTransactionsByUser(int userId, Operation operation,
                                            String category, LocalDate startDate, LocalDate endDate);
    int getCurrentMonthDebutCount(int userId);
    int getCurrentMonthCreditCount(int userId);

}
