package ru.yandex.practicum.filmorate.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class FilmorateApplicationTests {
    @Autowired
    private UserController userController;
    @Autowired
    private FilmController filmController;
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(userController);
        Assertions.assertNotNull(filmController);
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(filmService);
    }

    @Test
    void getUsersList() {
        User user = User.builder()
                .email("test@test.net")
                .login("testUser")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        userController.addNewUser(user);

        List<User> users = userController.getUsers().stream().toList();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user, users.getFirst());
    }
}
