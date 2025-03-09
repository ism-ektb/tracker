package ru.ism.repositiry;

import ru.ism.module.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionalRepository {
    void addTransaction(Transaction transaction);
    void removeTransaction(int transactionId);
    List<Transaction> getTransactionsByUserId(int userId);
    void updateTransaction(int id, Transaction transaction);
    Optional<Transaction> getTransaction(int transactionId);
}
