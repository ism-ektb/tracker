package ru.ism.repositiry;

import org.junit.jupiter.api.Test;
import ru.ism.module.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    private final UserRepository repository = new UserRepositoryImpl();

    @Test
    void getUser() {
        Optional<User> user = repository.getUser(1);
        assertTrue(user.isPresent());
        Optional<User> user2 = repository.getUser(2);
        assertTrue(user2.isEmpty());
    }

    @Test
    void getUserByEmail() {
        Optional<User> user = repository.getUserByEmail("admin@admin.com");
        assertTrue(user.isPresent());
        Optional<User> user2 = repository.getUserByEmail("admin@admin1.com");
        assertTrue(user2.isEmpty());
    }

    @Test
    void createUser() {
        User user = repository.createUser(new User("1", "1", "1"));
        assertNotNull(user);
    }

    @Test
    void updateUser() {
        User user = repository.createUser(new User("1", "1", "1"));
        User newUser = repository.updateUser(2, new User("2", "2", "2"));
        assertEquals(repository.getUser(2).get().getEmail(), "2");
    }

    @Test
    void deleteUser() {
        User user = repository.createUser(new User("1", "1", "1"));
        repository.deleteUser(2);
        assertTrue(repository.getUser(2).isEmpty());
    }

    @Test
    void updateUserLimit() {
        User user = repository.createUser(new User("1", "1", "1"));
        repository.updateUserLimit(2, 30000);
        assertEquals(repository.getUser(2).get().getMonthLimit(), 30000);
    }

}