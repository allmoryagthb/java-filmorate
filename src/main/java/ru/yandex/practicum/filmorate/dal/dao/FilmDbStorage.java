package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    protected JdbcTemplate jdbcTemplate;
    private static final String GET_FILMS = "SELECT * FROM films";
    private static final String INSERT_FILM = """
                INSERT INTO films (name, description, releaseDate, duration)
                VALUES (?, ?, ?, ?)
                """;
    private static final String INSERT_FILM_GENRES = """
                INSERT INTO film_genres (film_id, genre_id)
                VALUES (?, ?)
                """;
    private static final String INSERT_FILM_RATING = """
            INSERT INTO film_rating (film_id, rating_id)
            VALUES (?, ?)
            """;

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query(GET_FILMS, new FilmRowMapper(jdbcTemplate));
    }

    @Override
    public void addNewFilm(Film film) {
        // --- Добавление фильма в БД ---
        FilmDto filmDto = FilmMapper.jpaToDto(film);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_FILM, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, filmDto.getName());
            ps.setObject(2, filmDto.getDescription());
            ps.setObject(3, filmDto.getReleaseDate());
            ps.setObject(4, filmDto.getDuration());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        // Возвращаем id нового фильма
        if (id != null) {
            film.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }

        // --- Добавление жанров фильма в БД ---
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                int rows = jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
                if (rows < 1)
                    throw new InternalServerException("Не удалось сохранить данные");
            }
        }

        // --- Добавление рейтинга фильма в БД ---
        int rows = jdbcTemplate.update(INSERT_FILM_RATING, film.getId(), film.getRating().getId());
        if (rows < 1)
            throw new InternalServerException("Не удалось сохранить данные");
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
}
