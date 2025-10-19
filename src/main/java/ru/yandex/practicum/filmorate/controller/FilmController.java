package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final static LocalDate dateCheck = LocalDate.of(1895, 12, 28);
    private Long id = 0L;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("return all films");
        return films.values();
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        filmValidator(film);
        film.setId(++id);
        this.films.put(id, film);
        log.info("new film added to collection");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (Objects.isNull(film.getId()) || !films.containsKey(film.getId()))
            throw new CustomValidationException("incorrect id");
        filmValidator(film);
        films.put(film.getId(), film);
        log.info("film successfully updated");
        return film;
    }

    private void filmValidator(@Valid Film film) {
        if (film.getReleaseDate().isBefore(dateCheck)) {
            log.error("incorrect data - validation failed");
            throw new CustomValidationException("date is before " + dateCheck);
        }
    }
}
