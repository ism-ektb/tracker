package ru.ism.repositiry;

import org.junit.jupiter.api.Test;
import ru.ism.module.Operation;
import ru.ism.module.Transaction;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionalRepoImplTest {
    private TransactionalRepository repo = new TransactionalRepoImpl();

    @Test
    void addTransaction() {
        Transaction tx = new Transaction(Operation.DEBIT, 10, "1", "1", LocalDate.now());
        tx.setUserId(1);
        repo.addTransaction(tx);
        assertEquals(1, repo.getTransactionsByUserId(1).size());
    }

    @Test
    void removeTransaction() {
        Transaction tx = new Transaction(Operation.DEBIT, 10, "1", "1", LocalDate.now());
        tx.setUserId(1);
        repo.addTransaction(tx);
        repo.removeTransaction(1);
        assertEquals(0, repo.getTransactionsByUserId(1).size());
    }

    @Test
    void getTransaction() {
        Transaction tx = new Transaction(Operation.DEBIT, 10, "1", "1", LocalDate.now());
        tx.setUserId(1);
        repo.addTransaction(tx);
        assertTrue(repo.getTransaction(1).isPresent());
    }
}