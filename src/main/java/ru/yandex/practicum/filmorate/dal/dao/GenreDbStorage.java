package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.GenreStorage;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Optional<Genre> getGenreById(Long id) {
        String getGenreById = """
                SELECT *
                FROM genre
                WHERE id = ?
                """;
        List<Genre> genres = jdbcTemplate.query(getGenreById, (resultSet, rowNumber) ->
                Genre.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build(), id);
        if (genres.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(genres.getFirst());
    }
}
