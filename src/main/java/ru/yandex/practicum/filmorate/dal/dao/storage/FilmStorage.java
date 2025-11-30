package ru.yandex.practicum.filmorate.dal.dao.storage;

import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film getFilmById(Long id);

    void addNewFilm(Film film);

    FilmDto updateFilm(FilmDto filmDto);

    boolean checkFilmExistsById(Long filmId);

    void putLikeToFilm(Long filmId, Long userId);

    void removeLikeFromFilm(Long filmId, Long userId);
}
