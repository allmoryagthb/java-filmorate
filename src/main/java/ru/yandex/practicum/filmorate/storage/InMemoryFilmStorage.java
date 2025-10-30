package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final static Map<Long, Film> films = new HashMap<>();
    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);
    private Long id = 0L;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void addNewFilm(Film film) {
        filmValidator(film);
        film.setId(++id);
        films.put(id, film);
    }

    @Override
    public Film updateFilm(Film film) {
        if (Objects.isNull(film.getId()) || !films.containsKey(film.getId()))
            throw new ValidationException("incorrect id");
        filmValidator(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean checkFilmExistsById(Long filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public void putLikeToFilm(Long filmId, Long userId) {
        films.get(filmId).getLikesUsersIds().add(userId);
    }

    private void filmValidator(@Valid Film film) {
        if (film.getReleaseDate().isBefore(dateCheck)) {
            log.error("incorrect data - validation failed");
            throw new ValidationException("date is before " + dateCheck);
        }
    }
}
