package ru.ism;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest {
    Main main = new Main();
    Scanner sc = mock(Scanner.class);

    @Test
    void printTransactionMenu() {
        main.printTransactionMenu();

    }

    @Test
    void inputInt() {
        when(sc.next()).thenReturn("1");
        int i = main.inputInt(sc, "номер");
        assertEquals(1, i);
    }

    @Test
    void isInt() {
        assertTrue(main.isInt("1"));
        assertFalse(main.isInt("ty"));
    }

    @Test
    void isDate() {
        assertTrue(main.isDate("2024-01-01"));
        assertFalse(main.isDate("202-01-02"));
    }
}