package ru.yandex.practicum.filmorate.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = null;
    }

    @Test
    public void addNewUserWithValidParams() {
        User user = User.builder()
                .id(123L)
                .email("johndoe@mail.com")
                .login("jdoe123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.addNewUser(user);

        Assertions.assertTrue(userController.getUsers().contains(user));
    }

    @Test
    public void getAllUsersWhenEmpty() {
        Assertions.assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void getAllUsersWhenNotEmpty() {
        User user = User.builder()
                .email("johndoe@mail.com")
                .login("jdoe123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.addNewUser(user);

        Assertions.assertEquals(1, userController.getUsers().size());
        Assertions.assertTrue(userController.getUsers().contains(user));
    }

    @Test
    public void updateFilmWithValidParams() {
        userController.addNewUser(User.builder()
                .email("johndoe@mail.com")
                .login("jdoe123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());
        User userUpd = User.builder()
                .id(1L)
                .email("johndoeupd@mail.com")
                .login("jdoe123upd")
                .name("John_Dupd")
                .birthday(LocalDate.of(1988, 5, 12))
                .build();
        userController.updateUser(userUpd);

        Assertions.assertEquals(1, userController.getUsers().size());
        Assertions.assertTrue(userController.getUsers().contains(userUpd));
    }

    @Test
    public void addNewUserWithInvalidParams() {
        User invalidUser = User.builder()
                .email("johndoe@mail.com")
                .login("jdoe 123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.addNewUser(invalidUser));
    }

    @Test
    public void updateUserWithNonexistedId() {
        User userUpd = User.builder()
                .id(123456L)
                .email("johndoeupd@mail.com")
                .login("jdoe123upd")
                .name("John_Dupd")
                .birthday(LocalDate.of(1988, 5, 12))
                .build();
        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.updateUser(userUpd));
    }
}
