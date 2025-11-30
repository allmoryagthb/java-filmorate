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

    @Override
    public Collection<Rating> getAllRatings() {
        String getAllRatings = """
                SELECT *
                FROM rating
                ORDER BY id ASC
                """;
        return jdbcTemplate.query(getAllRatings, new RatingRowMapper());
    }

    @Override
    public Rating getRatingById(Long id) {
        String getRatingById = """
                SELECT *
                FROM rating
                WHERE id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(getRatingById, new RatingRowMapper(), id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("404 Not Found");
        }
    }
}
