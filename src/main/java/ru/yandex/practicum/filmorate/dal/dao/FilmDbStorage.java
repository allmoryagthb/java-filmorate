package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    protected JdbcTemplate jdbcTemplate;
    private final RowMapper<Film> rowMapper;

    private static final String GET_FILMS = """
            SELECT *
            FROM films AS f
                     LEFT JOIN film_mpa AS fm ON fm.film_id = f.id
                     LEFT JOIN mpa AS m ON m.id = fm.mpa_id
            ORDER BY f.id ASC
            """;
    private static final String GET_FILM_BY_ID = """
            SELECT *
            FROM films AS f
            LEFT JOIN film_mpa AS fm ON fm.film_id = f.id
            LEFT JOIN mpa AS m ON m.id = fm.mpa_id
            WHERE f.id = ?
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
    private static final String GET_POPULAR_FILMS = """
            SELECT f.id, f.name, f.description, f.releaseDate, f.duration, COUNT(fl.user_id) AS c, m.id, m.name
                FROM films AS f
                INNER JOIN film_likes AS fl ON f.id = fl.film_id
                LEFT JOIN film_mpa AS fm ON fm.film_id = f.id
                LEFT JOIN mpa AS m ON m.id = fm.mpa_id
                GROUP BY f.id
                ORDER BY c DESC
                LIMIT ?
            """;

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = jdbcTemplate.query(GET_FILMS, rowMapper);
        addGenresToFilms(films);
        return films;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        List<Film> films = jdbcTemplate.query(GET_FILM_BY_ID, (resultSet, rowNumber) ->
                filmBuilder(resultSet), id);
        if (films.isEmpty())
            return Optional.empty();
        addGenresToFilms(films);
        return Optional.of(films.getFirst());
    }

    @Override
    public void addNewFilm(Film film) {
        // --- Добавление фильма в БД ---
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_FILM, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
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
    public Film updateFilm(Film film) {
        jdbcTemplate.update(
                UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(int size) {
        return jdbcTemplate.query(GET_POPULAR_FILMS, (resultSet, rowNumber) ->
                filmBuilder(resultSet), size);
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

    private Film filmBuilder(ResultSet resultSet) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getLong("duration"))
                .releaseDate(resultSet.getObject("releaseDate", LocalDate.class))
                .mpa(new Mpa(resultSet.getLong("mpa.id"), resultSet.getString("mpa.name")))
                .build();
    }

    private void addGenresToFilms(List<Film> films) {
        List<Long> filmIds = films.stream()
                .map(Film::getId).toList();
        String filmsIdsQuestionsMarks = filmIds.stream().map(e -> "?").collect(Collectors.joining(","));

        String getFilmGenres = """
                SELECT fg.film_id, g.id, g.name
                FROM genre AS g
                INNER JOIN film_genres AS fg ON fg.genre_id = g.id
                WHERE fg.film_id IN (%s)
                ORDER BY fg.film_id ASC
                """.formatted(filmsIdsQuestionsMarks);

        Map<Long, Set<Genre>> filmGenres = films.stream()
                .collect(Collectors.toMap(Film::getId, Film::getGenres));

        jdbcTemplate.query(getFilmGenres, rs -> {
            long filmId = rs.getLong("film_genres.film_id");
            Genre g = Genre.builder()
                    .id(rs.getLong("genre.id"))
                    .name(rs.getString("genre.name"))
                    .build();
            filmGenres.get(filmId).add(g);
        }, filmIds.toArray());
    }
}
