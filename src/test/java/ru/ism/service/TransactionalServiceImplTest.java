package ru.ism.service;

import org.junit.jupiter.api.Test;
import ru.ism.module.Operation;
import ru.ism.module.Transaction;
import ru.ism.repositiry.TransactionalRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionalServiceImplTest {
    private TransactionalRepository repo = mock(TransactionalRepository.class);
    private UserService userService = mock(UserService.class);
    private final TransactionService txService = new TransactionalServiceImpl(repo, userService);

    @Test
    void addTransaction() {
        when(repo.getTransaction(anyInt())).thenReturn(null);
        when(userService.getBalance(anyInt())).thenReturn(15000);
        doNothing().when(userService).setBalance(anyInt(), anyInt());
        txService.addTransaction(1, new Transaction(Operation.CREDIT,
                100, "1", "1", LocalDate.now()));
        verify(repo, times(1)).addTransaction(any());
        verify(userService, times(1)).setBalance(anyInt(), anyInt());
        verify(userService, times(1)).getMonthLimit(anyInt());
    }

    @Test
    void removeTransaction() {
        Transaction tx = new Transaction(Operation.CREDIT,
                100, "1", "1", LocalDate.now());
        tx.setUserId(2);
        when(repo.getTransaction(anyInt())).thenReturn(Optional.of(tx));
        txService.removeTransaction(2, 1);
        verify(userService, times(1)).setBalance(anyInt(), anyInt());
    }

    @Test
    void updateTransaction() {
        Transaction tx = new Transaction(Operation.CREDIT,
                100, "1", "1", LocalDate.now());
        tx.setUserId(2);
        when(repo.getTransaction(anyInt())).thenReturn(Optional.of(tx));
        txService.updateTransaction(1, 1, tx);
        verify(userService, times(1)).getMonthLimit(anyInt());
    }

    @Test
    void getTransactionsByUser() {
        Transaction tx = new Transaction(Operation.CREDIT,
                100, "1", "1", LocalDate.now());
        tx.setUserId(2);
        when(txService.getTransactions(anyInt())).thenReturn(List.of(tx));
        assertEquals(txService.getTransactionsByUser(2, null, null, LocalDate.now(), LocalDate.now()), List.of(tx));
    }

    @Test
    void getCurrentMonthDebutCount() {
        Transaction tx = new Transaction(Operation.DEBIT,
                100, "1", "1", LocalDate.now());
        tx.setUserId(2);
        when(txService.getTransactions(anyInt())).thenReturn(List.of(tx));
        assertEquals(txService.getCurrentMonthDebutCount(2), 100);
    }

    @Test
    void getCurrentMonthCreditCount() {
        Transaction tx = new Transaction(Operation.CREDIT,
                100, "1", "1", LocalDate.now());
        tx.setUserId(2);
        when(txService.getTransactions(anyInt())).thenReturn(List.of(tx));
        assertEquals(txService.getCurrentMonthCreditCount(2), 100);

    }
}