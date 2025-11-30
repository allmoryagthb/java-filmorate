package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.RatingStorage;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@Slf4j
@Repository
@AllArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private JdbcTemplate jdbcTemplate;
    private final static String GET_ALL_RATINGS = """
            SELECT *
            FROM rating
            ORDER BY id ASC
            """;
    private final static String GET_RATING_BY_ID = """
            SELECT *
            FROM rating
            WHERE id = ?
            """;

    @Override
    public Collection<Rating> getAllRatings() {
        return jdbcTemplate.query(GET_ALL_RATINGS, new RatingRowMapper());
    }

    @Override
    public Rating getRatingById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_RATING_BY_ID, new RatingRowMapper(), id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("404 Not Found");
        }
    }
}
