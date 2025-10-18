package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        filmValidator(film);
        film.setId(++id);
        this.films.put(id, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (Objects.isNull(film.getId()) || !films.containsKey(film.getId()))
            throw new CustomValidationException("incorrect id");
        filmValidator(film);
        films.put(film.getId(), film);
        return film;
    }

    private void filmValidator(Film film) {
        if (Objects.isNull(film.getName()))
            throw new CustomValidationException("name is null");
        if (Objects.isNull(film.getDescription()))
            throw new CustomValidationException("description is null");
        if (Objects.isNull(film.getReleaseDate()))
            throw new CustomValidationException("releaseDate is null");
        if (Objects.isNull(film.getDuration()))
            throw new CustomValidationException("duration is null");
        if (film.getName().isBlank())
            throw new CustomValidationException("name is blank");
        if (film.getDescription().isBlank())
            throw new CustomValidationException("description is blank");
        if (film.getDescription().length() > 200)
            throw new CustomValidationException("description > 200");
        if (film.getDuration() < 1)
            throw new CustomValidationException("duration < 1");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new CustomValidationException("LocalDate is before 1895-12-28");
    }
}
