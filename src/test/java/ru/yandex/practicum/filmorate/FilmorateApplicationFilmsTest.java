package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class FilmorateApplicationFilmsTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FilmController filmController;
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserController userController;

    @BeforeEach
    public void init(@Qualifier("filmService") FilmService filmService) {
        this.filmController = new FilmController(filmService);
    }

    @Test
    void getFilms() {
        Film film = Film.builder()
                .name("testFilm1")
                .description("testFilm1Desc")
                .releaseDate(LocalDate.of(1991, 1, 1))
                .duration(123L)
                .build();
        film = filmController.addNewFilm(film);
        List<Film> films = filmController.getAllFilms().stream().toList();
        Assertions.assertNotNull(films);
        Assertions.assertEquals(film, films.get(Math.toIntExact(film.getId()) - 1));
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
                .id(film.getId())
                .name("testFilm1upd")
                .description("testFilm1Descupd")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(321L)
                .build();
        filmController.updateFilm(filmUpd);
        List<Film> films = filmController.getAllFilms().stream().toList();
        Assertions.assertNotNull(films);
        Assertions.assertEquals(filmUpd, films.get(Math.toIntExact(film.getId()) - 1));
    }
}
