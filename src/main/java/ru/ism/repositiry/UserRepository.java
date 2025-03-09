package ru.ism.repositiry;

import ru.ism.module.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUser(int id);
    Optional<User> getUserByEmail(String email);
    User createUser(User user);
    User updateUser(int userId, User user);
    void updateUserLimit(int userId, int limit);
    void deleteUser(int id);
    void setBalance(int userId, int amount);
    List<User> getUsers();
}
