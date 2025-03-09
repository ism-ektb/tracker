package ru.ism.service;

import ru.ism.module.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> addUser(String email, String username, String password);
    Optional<User> updateUser(int currentId, String email, String username, String password);
    Optional<User> registration(String email, String password);
    void setMonthLimit(int currentId, int monthLimit);
    int getBalance(int currentId);
    void setBalance(int currentId, int balance);
    void deleteUser(int currentId);
    int getMonthLimit(int currentId);
    Optional<User> getUser(int currentId);
    List<User> getUsers();
}
