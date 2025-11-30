package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.MpaStorage;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@Repository
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getAllMpas() {
        String getAllRatings = """
                SELECT *
                FROM mpa
                ORDER BY id ASC
                """;
        return jdbcTemplate.query(getAllRatings, new RatingRowMapper());
    }

    @Override
    public Mpa getMpaById(Long id) {
        String getMpaById = """
                SELECT *
                FROM mpa
                WHERE id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(getMpaById, new RatingRowMapper(), id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("404 Not Found");
        }
    }
}
