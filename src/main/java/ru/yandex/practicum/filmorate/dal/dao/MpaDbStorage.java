package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.MpaStorage;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Optional<Mpa> getMpaById(Long id) {
        String getMpaById = """
                SELECT *
                FROM mpa
                WHERE id = ?
                """;
        List<Mpa> mpaList = jdbcTemplate.query(getMpaById, (resultSet, rowNumber) ->
                Mpa.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build(), id);
        if (mpaList.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(mpaList.getFirst());
    }
}
