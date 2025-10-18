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

    @GetMapping
    public Collection<Film> getFilms() {
        if (films.isEmpty())
            throw new CustomValidationException("Коллекция фильмов пустая");
        return films.values();
    }

    @PostMapping
    public void addNewFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new CustomValidationException("Фильм с таким id уже существует");
        }
        filmValidator(film);
        this.films.put(film.getId(), film);
    }

    @PutMapping
    public void updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId()))
            throw new CustomValidationException("Фильма с таким id не существует");
        filmValidator(film);
        films.put(film.getId(), film);
    }

    private void filmValidator(Film film) {
        if (Objects.isNull(film.getId()) ||
                Objects.isNull(film.getName()) ||
                Objects.isNull(film.getDescription()) ||
                Objects.isNull(film.getReleaseDate()) ||
                Objects.isNull(film.getDuration()) ||
                film.getName().isBlank() ||
                film.getDescription().isBlank() ||
                film.getDescription().length() > 200 ||
                film.getDuration() < 1 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new CustomValidationException("Фильм не прошел валидацию");
    }
}
