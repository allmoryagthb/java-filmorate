package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

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
}
