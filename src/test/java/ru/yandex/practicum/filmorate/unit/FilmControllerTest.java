package ru.yandex.practicum.filmorate.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;
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
                .id(123L)
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build();
        Assertions.assertEquals(new ResponseEntity<>(film, HttpStatus.CREATED), filmController.addNewFilm(film),
                "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void getAllFilmsWhenEmpty() {
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap()),
                filmController.getFilms(), "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void getAllFilmsWhenNotEmpty() {
        Film film = Film.builder()
                .id(123L)
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build();
        filmController.addNewFilm(film);
        var response = filmController.getFilms();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(film, response.getBody().get(film.getId()));
    }

    @Test
    public void updateFilmWithValidParams() {
        filmController.addNewFilm(Film.builder()
                .id(123L)
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build());
        Film filmUpd = Film.builder()
                .id(123L)
                .name("name upd")
                .description("desc upd")
                .releaseDate(LocalDate.of(1999, 6, 15))
                .duration(54321L)
                .build();
        var response = filmController.updateFilm(filmUpd);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Данные фильма с id %s успешно обновлены".formatted(filmUpd.getId()), response.getBody());
    }

    @ParameterizedTest
    @MethodSource("invalidFilms")
    public void addNewFilmWithInvalidParams(Film invalidFilm) {
        var response = filmController.addNewFilm(invalidFilm);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Фильм не прошел валидацию", response.getBody());
    }

    @Test
    public void updateFilmWithNonexistedId() {
        var response = filmController.updateFilm(Film.builder()
                .id(1234567L)
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(123123L)
                .build());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Фильма с таким id не существует", response.getBody());
    }

    private static Stream<Film> invalidFilms() {
        return Stream.of(
                Film.builder()
                        .name("name")
                        .description("desc")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .id(123L)
                        .description("desc")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .id(123L)
                        .name("name")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .id(123L)
                        .name("name")
                        .description("desc")
                        .releaseDate(LocalDate.of(1, 10, 10))
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .id(123L)
                        .name("name")
                        .description("desc")
                        .duration(123123L)
                        .build(),
                Film.builder()
                        .id(123L)
                        .name("name")
                        .description("desc")
                        .releaseDate(LocalDate.of(2000, 10, 10))
                        .build()
        );
    }
}
