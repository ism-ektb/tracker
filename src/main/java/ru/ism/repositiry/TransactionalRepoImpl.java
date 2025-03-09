package ru.ism.repositiry;

import ru.ism.module.Transaction;
import ru.ism.module.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionalRepoImpl implements TransactionalRepository {
    private Map<Integer, Transaction> transactions = new HashMap<>();
    private int lastId = 1;

    @Override
    public void addTransaction(Transaction transaction) {
        transaction.setId(lastId++);
        transactions.put(transaction.getId(), transaction);
        System.out.println("Transaction " + transaction.getId() + " added");
    }

    @Override
    public void removeTransaction(int transactionId) {
        transactions.remove(transactionId);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTransaction(int id, Transaction transaction) {
        transaction.setId(id);
        transactions.put(id, transaction);
        System.out.println("Transaction " + transaction.getId() + " updated");
    }

    @Override
    public Optional<Transaction> getTransaction(int transactionId) {
        if (!transactions.containsKey(transactionId)) {
            return Optional.empty();
        }
        return Optional.of(transactions.get(transactionId));
    }
}
