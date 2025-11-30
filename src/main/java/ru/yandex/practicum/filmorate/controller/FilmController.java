package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable(value = "filmId") Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addNewFilm(@Valid @RequestBody Film film) {
        return filmService.addNewFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto updateFilm(@Valid @RequestBody FilmDto film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void putLikeToFilm(
            @PathVariable(value = "filmId") Long filmId,
            @PathVariable(value = "userId") Long userId) {
        filmService.putLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLikeFromFilm(
            @PathVariable(value = "filmId") Long filmId,
            @PathVariable(value = "userId") Long userId) {
        filmService.removeLikeFromFilm(filmId, userId);
    }
}
