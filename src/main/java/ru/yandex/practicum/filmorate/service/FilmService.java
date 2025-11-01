package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);

    public Collection<Film> getAllFilms() {
        log.info("Вернуть все фильмы");
        return filmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        filmValidator(film);
        filmStorage.addNewFilm(film);
        log.info("Добавлен новый фильм с id = {}", film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        if (Objects.isNull(film.getId()))
            throw new ValidationException("Некорректный id");
        if (!filmStorage.checkFilmExistsById(film.getId()))
            throw new EntityNotFoundException("Фильма с id = %s не существует".formatted(film.getId()));
        filmValidator(film);
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Фильм с id = {} успешно обновлен", film.getId());
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

    private void filmValidator(@Valid Film film) {
        if (film.getReleaseDate().isBefore(dateCheck)) {
            log.error("incorrect data - validation failed");
            throw new ValidationException("date is before " + dateCheck);
        }
    }

    public Collection<Film> getPopular(Integer count) {
        if (count < 1)
            throw new ValidationException("Значение count должно быть больше нуля");
        return filmStorage.getAllFilms()
                .stream()
                .sorted(Comparator.comparingLong(Film::getRating).reversed())
                .limit(count)
                .toList();
    }

    private void storagesValidator(Long filmId, Long userId) {
        if (!filmStorage.checkFilmExistsById(filmId))
            throw new EntityNotFoundException("Фильма с id = %d не существует".formatted(filmId));
        if (!userStorage.checkUserExistsById(userId))
            throw new EntityNotFoundException("Пользователя с id = %d не существует".formatted(userId));
    }
}
