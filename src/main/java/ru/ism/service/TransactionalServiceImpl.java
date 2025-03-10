package ru.ism.service;

import ru.ism.module.Operation;
import ru.ism.module.Transaction;
import ru.ism.repositiry.TransactionalRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TransactionalServiceImpl implements TransactionService{

    private final TransactionalRepository repository;
    private final UserService userService;
    public TransactionalServiceImpl(TransactionalRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }
    @Override
    public void addTransaction(int userId, Transaction transaction) {
        transaction.setUserId(userId);
        repository.addTransaction(transaction);
        int balance = userService.getBalance(userId);
        int delta = transaction.getOperation() == Operation.CREDIT ? -1 : 1;
        balance += delta * transaction.getSum();
        userService.setBalance(userId, balance);

        List<Transaction> monthCredit = getTransactionsByUser(userId, Operation.CREDIT,
                null, transaction.getDate().withDayOfMonth(1),
                transaction.getDate());
        int monthSum = monthCredit.stream().mapToInt(Transaction::getSum).sum();
        if (monthSum > userService.getMonthLimit(userId)) {
            System.out.println("Month limit exceeded");
        }
    }

    @Override
    public void removeTransaction(int userId, int id) {
        Optional<Transaction> transaction = repository.getTransaction(id);
        if (transaction.isEmpty()){
            System.out.println("Transaction not found");
            return;
        }
        if (transaction.get().getUserId() != userId){
            System.out.println("Transaction not in use");
            return;
        }
        repository.removeTransaction(id);
        int balance = userService.getBalance(userId);
        int delta = transaction.get().getOperation() == Operation.CREDIT ? -1 : 1;
        balance -= delta * transaction.get().getSum();
        userService.setBalance(userId, balance);
    }

    @Override
    public List<Transaction> getTransactions(int userId) {
        return repository.getTransactionsByUserId(userId);
    }

    @Override
    public void updateTransaction(int userId, int id, Transaction transaction) {
        Optional<Transaction> old = repository.getTransaction(id);
        if (old.isEmpty()) {
            System.out.println("Transaction not found");
            return;
        }
        removeTransaction(userId, id);
        transaction.setUserId(userId);
        repository.updateTransaction(id, transaction);
        int balance = userService.getBalance(userId);
        int delta = transaction.getOperation() == Operation.CREDIT ? 1 : -1;
        balance += delta * transaction.getSum();
        userService.setBalance(userId, balance);

        List<Transaction> monthCredit = getTransactionsByUser(userId, Operation.CREDIT,
                null, transaction.getDate().withDayOfMonth(1),
                transaction.getDate());
        int monthSum = monthCredit.stream().mapToInt(Transaction::getSum).sum();
        if (monthSum > userService.getMonthLimit(userId)) {
            System.out.println("Month limit exceeded");
        }
    }

    @Override
    public List<Transaction> getTransactionsByUser(int userId, Operation operation, String category,
                                                   LocalDate startDate, LocalDate endDate) {
        return getTransactions(userId).stream()
        .filter(transaction -> (transaction.getOperation() == operation || operation == null))
                .filter(transaction -> (transaction.getCategory().equals(category) || category == null))
                .filter(transaction -> (startDate == null || transaction.getDate().isAfter(startDate.minusDays(1))))
                .filter(transaction -> (endDate == null || transaction.getDate().isBefore(endDate.plusDays(1))))
        .toList();
    }

    @Override
    public int getCurrentMonthDebutCount(int userId) {
        return getTransactionsByUser(userId, Operation.DEBIT, null,
                LocalDate.now().withDayOfMonth(1), LocalDate.now()).stream().mapToInt(Transaction::getSum).sum();
    }

    @Override
    public int getCurrentMonthCreditCount(int userId) {
        return getTransactionsByUser(userId, Operation.CREDIT, null,
                LocalDate.now().withDayOfMonth(1), LocalDate.now()).stream().mapToInt(Transaction::getSum).sum();
    }
    }

