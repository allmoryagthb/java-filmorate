package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Override
    public Collection<Film> getAllFilms() {
        return List.of();
    }

    @Override
    public void addNewFilm(Film film) {

    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public boolean checkFilmExistsById(Long filmId) {
        return false;
    }

    @Override
    public void putLikeToFilm(Long filmId, Long userId) {

    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {

    }
//    private final Map<Long, Film> films = new HashMap<>();
//    private Long id = 0L;
//
//    @Override
//    public Collection<FilmDto> getAllFilms() {
//        return films.values();
//    }
//
//    @Override
//    public void addNewFilm(Film film) {
//        film.setId(++id);
//        films.put(id, film);
//    }
//
//    @Override
//    public Film updateFilm(Film film) {
//        films.put(film.getId(), film);
//        return film;
//    }
//
//    @Override
//    public boolean checkFilmExistsById(Long filmId) {
//        return films.containsKey(filmId);
//    }
//
//    @Override
//    public void putLikeToFilm(Long filmId, Long userId) {
//        films.get(filmId).getLikesUsersIds().add(userId);
//    }
//
//    @Override
//    public void removeLikeFromFilm(Long filmId, Long userId) {
//        films.get(filmId).getLikesUsersIds().remove(userId);
//    }
}
