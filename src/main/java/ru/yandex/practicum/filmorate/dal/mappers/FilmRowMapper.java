package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final String GET_GENRES_QUERY = """
            SELECT *
            FROM genre AS g
            INNER JOIN film_genres AS fg ON g.id = fg.genre_id
            WHERE fg.film_id = ?
            ORDER BY fg.genre_id ASC
            """;
    private final String GET_LIKES_QUERY = """
            SELECT fl.user_id
            FROM film_likes AS fl
            WHERE fl.film_id = ?
            ORDER BY fl.user_id ASC
            """;
    private final String GET_RATING_MPAA_QUERY = """
            SELECT *
            FROM mpa
            INNER JOIN film_mpa AS fm ON mpa.id = fm.mpa_id
            WHERE fm.film_id = ?
            ORDER BY fm.mpa_id ASC
            """;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Set<Long> userIdsLikes = new HashSet<>(jdbcTemplate
                .queryForList(GET_LIKES_QUERY, Long.class, rs.getLong("id")));
        Set<Genre> genres = new LinkedHashSet<>((jdbcTemplate
                .query(GET_GENRES_QUERY, new GenreRowMapper(),
                        rs.getLong("id"))
                .stream()
                .sorted(Comparator.comparing(Genre::getId))
                .toList()));
        Mpa rating;
        try {
            rating = jdbcTemplate.queryForObject(GET_RATING_MPAA_QUERY, new RatingRowMapper(), rs.getLong("id"));
        } catch (EmptyResultDataAccessException e) {
            rating = null;
        }

        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getObject("releaseDate", LocalDate.class))
                .duration(rs.getLong("duration"))
                .mpa(rating)
                .build();

        for (Long id : userIdsLikes)
            film.addLikesUsersId(id);

        for (Genre genre : genres)
            film.addGenre(genre);

        return film;
    }
}
