package ru.yandex.practicum.filmorate.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.stream.Stream;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
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

    @ParameterizedTest
    @MethodSource("invalidUsers")
    public void addNewUserWithInvalidParams(User invalidUser) {
        Assertions.assertThrows(CustomValidationException.class, () -> userController.addNewUser(invalidUser));
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
        Assertions.assertThrows(CustomValidationException.class, () -> userController.updateUser(userUpd));
    }

    private static Stream<User> invalidUsers() {
        return Stream.of(
                User.builder()
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .email("это-неправильный?эмейл@")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .email("johndoeupd@mail.com")
                        .login("jdoe123 upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .email("johndoeupd@mail.com")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(2999, 12, 12))
                        .build(),
                User.builder()
                        .email("johndoeupd@mail.com")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .build()
        );
    }
}
