package ru.ism.service;

import org.junit.jupiter.api.Test;
import ru.ism.module.User;
import ru.ism.repositiry.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository);

    @Test
    void addUser() {
        when(userRepository.createUser(any())).thenReturn(new User("1", "1", "1"));
        assertTrue(userService.addUser("1", "1", "1").isPresent());
    }

    @Test
    void updateUser() {
        User user = new User("1", "1", "1");
        user.setId(2);
        when(userRepository.getUser(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.updateUser(anyInt(), any())).thenReturn(new User("1", "1", "1"));
        assertTrue(userService.updateUser(1, "1", "1", "1").isPresent());
    }

    @Test
    void registration() {
        User user = new User("1", "1", "1");
        user.setId(2);
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        assertTrue(userService.registration("1", "1").isPresent());
    }

    @Test
    void getUser() {
        User user = new User("1", "1", "1");
        user.setId(2);
        when(userRepository.getUser(anyInt())).thenReturn(Optional.of(user));
        assertTrue(userService.getUser(2).isPresent());
    }

    @Test
    void deleteUser() {
        userRepository.deleteUser(1);
    }

    @Test
    void getAllUsers() {
        when(userRepository.getUsers()).thenReturn(List.of());
        assertEquals(userService.getUsers().size(), 0);
    }

    @Test
    void getBalance() {
        when(userRepository.getUser(anyInt())).thenReturn(Optional.of(new User("1", "1", "1")));
        assertEquals(0, userService.getBalance(1));
    }

    @Test
    void setMouthLimit() {
        when(userRepository.getUser(anyInt())).thenReturn(Optional.of(new User("1", "1", "1")));
        userService.setMonthLimit(1, 100);
    }

}