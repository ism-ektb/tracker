package ru.ism.service;

import ru.ism.module.User;
import ru.ism.repositiry.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> addUser(String email, String username, String password) {
        if (userRepository.getUserByEmail(email).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(userRepository.createUser(new User(email, username, password)));
    }

    @Override
    public Optional<User> updateUser(int currentId, String email, String username, String password) {
        Optional<User> user = userRepository.getUser(currentId);
        if (user.isEmpty()) return Optional.empty();
        if (!user.get().getEmail().equals(email) && userRepository.getUserByEmail(email).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(userRepository.updateUser(user.get().getId(), new User(email, username, password)));
    }

    @Override
    public Optional<User> registration(String email, String password) {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) return Optional.empty();
        if (!user.get().getPassword().equals(password)) {return Optional.empty();}
        return user;
    }

    @Override
    public void setMonthLimit(int currentId, int monthLimit) {
        User user = userRepository.getUser(currentId).get();
        userRepository.updateUserLimit(user.getId(), monthLimit);
    }

    @Override
    public int getBalance(int currentId) {
        User user = userRepository.getUser(currentId).get();
        return user.getBalance();
    }

    @Override
    public void deleteUser(int currentId) {
        userRepository.deleteUser(currentId);
    }

    @Override
    public void setBalance(int currentId, int balance) {
        userRepository.setBalance(currentId, balance);
    }

    @Override
    public int getMonthLimit(int currentId) {
        return userRepository.getUser(currentId).get().getMonthLimit();
    }

    @Override
    public Optional<User> getUser(int currentId) {
        if (userRepository.getUser(currentId).isPresent()) {
            return Optional.of(userRepository.getUser(currentId).get());
        }
        return Optional.empty();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }
}
