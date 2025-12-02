package ru.yandex.practicum.filmorate.dal.dao.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(Long id);

    void addNewFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getPopularFilms(int size);

    boolean checkFilmExistsById(Long filmId);

    void putLikeToFilm(Long filmId, Long userId);

    void removeLikeFromFilm(Long filmId, Long userId);
}
