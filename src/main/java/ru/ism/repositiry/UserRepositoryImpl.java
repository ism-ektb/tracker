package ru.ism.repositiry;

import ru.ism.module.Role;
import ru.ism.module.User;

import java.util.*;

public class UserRepositoryImpl implements UserRepository {

    private Map<Integer, User> users = new HashMap<>();
    private int lastId = 2;

    public UserRepositoryImpl() {
        User admin = new User("admin@admin.com", "admin", "admin");
        admin.setRole(Role.ADMIN);
        admin.setId(1);
        users.put(1, admin);
    }

    @Override
    public Optional<User> getUser(int id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public User createUser(User user) {
        user.setId(lastId);
        users.put(user.getId(), user);
        System.out.println("Created user Id: " + lastId++);
        return user;
    }

    @Override
    public User updateUser(int userId, User patchUser) {
        User user = users.get(userId);
        user.setName(patchUser.getName());
        user.setEmail(patchUser.getEmail());
        user.setPassword(patchUser.getPassword());
        users.replace(user.getId(), user);
        System.out.println("Updated user Id: " + user.getId());
        System.out.println(users.get(userId));
        return user;
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            System.out.println("User does not exist");
            return;
        }
        users.remove(id);
        System.out.println("Deleted user Id: " + id);
    }

    @Override
    public void updateUserLimit(int userId, int limit) {
        User user = users.get(userId);
        user.setMonthLimit(limit);
        users.replace(userId, user);
    }

    @Override
    public void setBalance(int userId, int amount) {
        users.get(userId).setBalance(amount);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}

