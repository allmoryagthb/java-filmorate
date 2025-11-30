package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.GenreStorage;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Repository
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    protected JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAllGenres() {
        String getAllGenres = """
                SELECT *
                FROM genre
                ORDER BY id ASC
                """;
        return jdbcTemplate.query(getAllGenres, new GenreRowMapper());
    }

    @Override
    public Genre getGenreById(Long id) {
        String getGenreById = """
                SELECT *
                FROM genre
                WHERE id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(getGenreById, new GenreRowMapper(), id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("404 Not Found - No Such Genre Id");
        }
    }
}
