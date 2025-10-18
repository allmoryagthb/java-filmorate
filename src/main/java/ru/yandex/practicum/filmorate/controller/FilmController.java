package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<Map<Long, Film>> getFilms() {
        if (films.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());

        return ResponseEntity.ok(films);
    }

    @PostMapping
    public ResponseEntity<?> addNewFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Фильм с таким id уже существует");
        }
        try {
            filmValidator(film);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Фильм не прошел валидацию");
        }
        this.films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Фильма с таким id не существует");
        try {
            filmValidator(film);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Фильм не прошел валидацию");
        }
        films.put(film.getId(), film);
        return ResponseEntity.ok("Данные фильма с id %s успешно обновлены".formatted(film.getId()));
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
            throw new ValidationException("Фильм не прошел валидацию");
    }
}
