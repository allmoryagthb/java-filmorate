package ru.yandex.practicum.filmorate.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.stream.Stream;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
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

        Assertions.assertTrue(filmController.getFilms().contains(film));
    }

    @Test
    public void getAllFilmsWhenEmpty() {
        Assertions.assertTrue(filmController.getFilms().isEmpty());
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
        var response = filmController.getFilms();
        Assertions.assertTrue(response.contains(film));
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

        Assertions.assertTrue(filmController.getFilms().contains(filmUpd));
    }

    @ParameterizedTest
    @MethodSource("invalidFilms")
    public void addNewFilmWithInvalidParams(Film invalidFilm) {
        Assertions.assertThrows(CustomValidationException.class, () -> filmController.addNewFilm(invalidFilm));
    }

    @Test
    public void updateFilmWithNonexistedId() {
        Assertions.assertThrows(CustomValidationException.class, () -> filmController.updateFilm(Film.builder()
                .id(1234567L)
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build()));
    }

    private static Stream<Film> invalidFilms() {
        return Stream.of(
                Film.builder()
                        .description("desc")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .name("name")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .name("name")
                        .description("desc")
                        .releaseDate(LocalDate.of(1, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .name("name")
                        .description("desc")
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .name("name")
                        .description("desc")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .build()
        );
    }
}
