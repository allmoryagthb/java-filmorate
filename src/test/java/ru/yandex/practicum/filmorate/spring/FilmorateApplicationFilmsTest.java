package ru.yandex.practicum.filmorate.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
public class FilmorateApplicationFilmsTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private FilmController filmController;
    private UserController userController;

    @BeforeEach
    void setUp() {
        filmController = null;
        userController = null;
    }

    @Test
    void getFilms() {
        Film film = Film.builder()
                .name("testFilm1")
                .description("testFilm1Desc")
                .releaseDate(LocalDate.of(1991, 1, 1))
                .duration(123L)
                .build();
        filmController.addNewFilm(film);

//        List<FilmDto> films = filmController.getAllFilms().stream().toList();
//        Assertions.assertNotNull(films);
//        Assertions.assertEquals(1, films.size());
//        Assertions.assertEquals(film, films.getFirst());
    }

    @Test
    void updateFilm() {
        Film film = Film.builder()
                .name("testFilm1")
                .description("testFilm1Desc")
                .releaseDate(LocalDate.of(1991, 1, 1))
                .duration(123L)
                .build();
        filmController.addNewFilm(film);

        Film filmUpd = Film.builder()
                .id(1L)
                .name("testFilm1upd")
                .description("testFilm1Descupd")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(321L)
                .build();
        //filmController.updateFilm(filmUpd);
//        List<FilmDto> films = filmController.getAllFilms().stream().toList();
//        Assertions.assertNotNull(films);
//        Assertions.assertEquals(1, films.size());
//        Assertions.assertEquals(filmUpd, films.getFirst());
    }
}
