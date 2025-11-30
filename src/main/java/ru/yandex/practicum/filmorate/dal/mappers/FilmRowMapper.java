package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
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
            FROM rating AS r
            INNER JOIN film_rating AS fr ON r.id = fr.rating_id
            WHERE fr.film_id = ?
            ORDER BY fr.rating_id ASC
            """;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Set<Long> userIdsLikes = new HashSet<>(jdbcTemplate
                .queryForList(GET_LIKES_QUERY, Long.class, rs.getLong("id")));
        Set<Genre> genres = new HashSet<>(jdbcTemplate
                .query(GET_GENRES_QUERY, new GenreRowMapper(), rs.getLong("id")));
        Rating rating;
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
                .rating(rating)
                .build();

        for (Long id : userIdsLikes)
            film.addLikesUsersId(id);

        for (Genre genre : genres)
            film.addGenre(genre);

        return film;
    }
}
