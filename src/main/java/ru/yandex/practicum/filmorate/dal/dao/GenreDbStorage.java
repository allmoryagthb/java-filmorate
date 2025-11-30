package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.GenreStorage;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Repository
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    protected JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAllGenres() {
        String GET_ALL_GENRES = """
                SELECT *
                FROM genre
                ORDER BY id ASC
                """;
        return jdbcTemplate.query(GET_ALL_GENRES, new GenreRowMapper());
    }

    @Override
    public Genre getGenreById(Long id) {
        String GET_GENRE_BY_ID = """
                SELECT *
                FROM genre
                WHERE id = ?
                """;
        return jdbcTemplate.queryForObject(GET_GENRE_BY_ID, new GenreRowMapper(), id);
    }
}
