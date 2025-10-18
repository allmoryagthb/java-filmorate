package ru.yandex.practicum.filmorate.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
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
        var response = userController.addNewUser(user);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void getAllUsersWhenEmpty() {
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap()),
                userController.getUsers(), "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void getAllUsersWhenNotEmpty() {
        User user = User.builder()
                .id(123L)
                .email("johndoe@mail.com")
                .login("jdoe123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.addNewUser(user);

        var response = userController.getUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody().get(user.getId()));
    }

    @Test
    public void updateFilmWithValidParams() {
        userController.addNewUser(User.builder()
                .id(123L)
                .email("johndoe@mail.com")
                .login("jdoe123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());
        User userUpd = User.builder()
                .id(123L)
                .email("johndoeupd@mail.com")
                .login("jdoe123upd")
                .name("John_Dupd")
                .birthday(LocalDate.of(1988, 5, 12))
                .build();
        var response = userController.updateUser(userUpd);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Данные пользователя с id %s успешно обновлены".formatted(userUpd.getId()), response.getBody());
    }

    @ParameterizedTest
    @MethodSource("invalidUsers")
    public void addNewUserWithInvalidParams(User invalidUser) {
        var response = userController.addNewUser(invalidUser);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Пользователь не прошел валидацию", response.getBody());
    }

    @Test
    public void updateUserWithNonexistedId() {
        User user = User.builder()
                .id(123L)
                .email("johndoe@mail.com")
                .login("jdoe123")
                .name("John_D")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userController.addNewUser(user);
        User userUpd = User.builder()
                .id(123456L)
                .email("johndoeupd@mail.com")
                .login("jdoe123upd")
                .name("John_Dupd")
                .birthday(LocalDate.of(1988, 5, 12))
                .build();
        var response = userController.updateUser(userUpd);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Пользователя с таким id не существует", response.getBody());
    }

    private static Stream<User> invalidUsers() {
        return Stream.of(
                User.builder()
                        .email("johndoeupd@mail.com")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .id(123L)
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .id(123L)
                        .email("это-неправильный?эмейл@")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .id(123L)
                        .email("johndoeupd@mail.com")
                        .login("jdoe123 upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(1988, 5, 12))
                        .build(),
                User.builder()
                        .id(123L)
                        .email("johndoeupd@mail.com")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .birthday(LocalDate.of(2999, 12, 12))
                        .build(),
                User.builder()
                        .id(123L)
                        .email("johndoeupd@mail.com")
                        .login("jdoe123upd")
                        .name("John_Dupd")
                        .build()
        );
    }
}
