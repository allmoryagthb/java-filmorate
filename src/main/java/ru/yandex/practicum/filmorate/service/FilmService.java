package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        log.info("return all films");
        return filmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        filmStorage.addNewFilm(film);
        log.info("new film added to collection");
        return film;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("film successfully updated");
        return updatedFilm;
    }

    public void putLikeToFilm(Long filmId, Long userId) {
        if (!filmStorage.checkFilmExistsById(filmId))
            throw new EntityNotFoundException("Фильма с таким id не существует");
        if (!userStorage.checkUserExistsById(userId))
            throw new EntityNotFoundException("Пользователя с таким id не существует");

        filmStorage.putLikeToFilm(filmId, userId);
    }
}
