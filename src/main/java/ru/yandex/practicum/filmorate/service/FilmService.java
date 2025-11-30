package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.storage.FilmStorage;
import ru.yandex.practicum.filmorate.dal.dao.storage.UserStorage;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);

    public FilmService(
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        log.info("Вернуть все фильмы");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        try {
            return filmStorage.getFilmById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("404 Not Found");
        }
    }

    public Film addNewFilm(Film film) {
        filmValidator(film);
        filmStorage.addNewFilm(film);
        log.info("Добавлен новый фильм с id = {}", film.getId());
        return film;
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        if (Objects.isNull(filmDto.getId()))
            throw new ValidationException("Некорректный id");
        if (!filmStorage.checkFilmExistsById(filmDto.getId()))
            throw new EntityNotFoundException("Фильма с id = %s не существует".formatted(filmDto.getId()));
        filmDtoValidator(filmDto);
        FilmDto updatedFilm = filmStorage.updateFilm(filmDto);
        log.info("Фильм с id = {} успешно обновлен", filmDto.getId());
        return updatedFilm;
    }

    public void putLikeToFilm(Long filmId, Long userId) {
        storagesValidator(filmId, userId);
        filmStorage.putLikeToFilm(filmId, userId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        storagesValidator(filmId, userId);
        filmStorage.removeLikeFromFilm(filmId, userId);
    }

    public Collection<Film> getPopular(Integer count) {
        if (count < 1)
            throw new ValidationException("Значение count должно быть больше нуля");
        return null;
    }

    private void filmValidator(@Valid Film film) {
        if (film.getReleaseDate().isBefore(dateCheck)) {
            log.error("incorrect data - validation failed");
            throw new ValidationException("date is before " + dateCheck);
        }
    }

    private void filmDtoValidator(@Valid FilmDto filmDto) {
        if (filmDto.getReleaseDate().isBefore(dateCheck)) {
            log.error("incorrect data - validation failed");
            throw new ValidationException("date is before " + dateCheck);
        }
    }

    private void storagesValidator(Long filmId, Long userId) {
        if (!filmStorage.checkFilmExistsById(filmId))
            throw new EntityNotFoundException("Фильма с id = %d не существует".formatted(filmId));
        if (!userStorage.checkUserExistsById(userId))
            throw new EntityNotFoundException("Пользователя с id = %d не существует".formatted(userId));
    }
}
