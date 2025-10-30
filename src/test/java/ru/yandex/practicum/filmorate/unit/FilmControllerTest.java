package ru.yandex.practicum.filmorate.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    public void addNewFilmWithValidParams() {
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build();
        filmController.addNewFilm(film);

     //   Assertions.assertTrue(filmController.getAllFilms().contains(film));
    }

    @Test
    public void getAllFilmsWhenEmpty() {
     //   Assertions.assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    public void getAllFilmsWhenNotEmpty() {
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build();
        filmController.addNewFilm(film);
        var response = filmController.getAllFilms();
     //   Assertions.assertTrue(response.contains(film));
    }

    @Test
    public void updateFilmWithValidParams() {
        filmController.addNewFilm(Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build());
        Film filmUpd = Film.builder()
                .id(1L)
                .name("name upd")
                .description("desc upd")
                .releaseDate(LocalDate.of(1999, 6, 15))
                .duration(54321L)
                .build();
        filmController.updateFilm(filmUpd);

      //  Assertions.assertTrue(filmController.getAllFilms().contains(filmUpd));
    }

    @Test
    public void addNewFilmWithInvalidParams() {
        Film invalidFilm = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(10, 10, 10))
                .duration(123123L)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmController.addNewFilm(invalidFilm));
    }

    @Test
    public void updateFilmWithNonexistedId() {
        Assertions.assertThrows(ValidationException.class, () -> filmController.updateFilm(Film.builder()
                .id(1234567L)
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build()));
    }
}
