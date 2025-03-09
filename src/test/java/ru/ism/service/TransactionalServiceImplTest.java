package ru.ism.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ism.repositiry.TransactionalRepository;

import static org.junit.jupiter.api.Assertions.*;

class TransactionalServiceImplTest {
    private TransactionalRepository repo = Mockito.mock(TransactionalRepository.class);
    private UserService userService = Mockito.mock(UserService.class);
    private final TransactionService txService = new TransactionalServiceImpl(repo, userService);

    @Test
    void addTransaction() {
    }

    @Test
    void removeTransaction() {
    }

    @Test
    void getTransactions() {
    }

    @Test
    void updateTransaction() {
    }

    @Test
    void getTransactionsByUser() {
    }

    @Test
    void getCurrentMonthDebutCount() {
    }

    @Test
    void getCurrentMonthCreditCount() {
    }
}