package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.FilmStorage;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    protected JdbcTemplate jdbcTemplate;
    private static final String GET_FILMS = """
            SELECT *
            FROM films
            ORDER BY films.id ASC
            """;
    private static final String GET_FILM_BY_ID = """
            SELECT *
            FROM films
            WHERE id = ?
            """;
    private static final String INSERT_FILM = """
            INSERT INTO films (name, description, releaseDate, duration)
            VALUES (?, ?, ?, ?)
            """;
    private static final String INSERT_FILM_GENRES = """
            INSERT INTO film_genres (film_id, genre_id)
            VALUES (?, ?)
            """;
    private static final String INSERT_FILM_RATING = """
            INSERT INTO film_mpa (film_id, mpa_id)
            VALUES (?, ?)
            """;
    private static final String UPDATE_FILM = """
            UPDATE films
            SET name = ?,
                description = ?,
                releasedate = ?,
                duration = ?
            WHERE id = ?
            """;
    private static final String GET_FILM_COUNT_BY_ID = """
            SELECT COUNT(*)
            FROM films
            WHERE films.id = ?
            """;
    private static final String ADD_LIKE_TO_FILM = """
            INSERT INTO film_likes (film_id, user_id)
            VALUES (?, ?)
            """;
    private static final String REMOVE_LIKE_FROM_FILM = """
            DELETE FROM film_likes
            WHERE film_id = ? AND user_id = ?
            """;

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query(GET_FILMS, new FilmRowMapper(jdbcTemplate));
    }

    @Override
    public Film getFilmById(Long id) {
        return jdbcTemplate.queryForObject(GET_FILM_BY_ID, new FilmRowMapper(jdbcTemplate), id);
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
            film.setGenres(film.getGenres().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new)));
            for (Genre genre : film.getGenres()) {
                try {
                    jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
                } catch (DataAccessException e) {
                    throw new EntityNotFoundException("404 Not Found - No Such Genre");
                }
            }
        }

        // --- Добавление рейтинга фильма в БД ---
        if (film.getMpa() != null) {
            try {
                jdbcTemplate.update(INSERT_FILM_RATING, film.getId(), film.getMpa().getId());
            } catch (DataAccessException e) {
                throw new EntityNotFoundException("404 Not Found - No Such Mpa");
            }
        }
    }

    @Override
    public FilmDto updateFilm(FilmDto filmDto) {
        jdbcTemplate.update(
                UPDATE_FILM,
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                filmDto.getId());
        return filmDto;
    }

    @Override
    public boolean checkFilmExistsById(Long filmId) {
        return jdbcTemplate.queryForObject(GET_FILM_COUNT_BY_ID, Integer.class, filmId) == 1;
    }

    @Override
    public void putLikeToFilm(Long filmId, Long userId) {
        if (jdbcTemplate.update(ADD_LIKE_TO_FILM, filmId, userId) != 1)
            throw new InternalServerException("Не удалось поставить лайк");
    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        if (jdbcTemplate.update(REMOVE_LIKE_FROM_FILM, filmId, userId) != 1)
            throw new InternalServerException("Не удалось убрать лайк");
    }
}
