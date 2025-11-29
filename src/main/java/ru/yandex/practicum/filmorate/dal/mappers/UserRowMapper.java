package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_FRIENDS_ID = """
            SELECT friend_id
            FROM friends
            WHERE id = ?
            """;

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getObject("birthday", LocalDate.class))
                .build();
        Set<Long> ids = new HashSet<>(jdbcTemplate.queryForList(GET_FRIENDS_ID, Long.class, user.getId()));
        for (Long id : ids)
            user.addIdToFriends(id);
        return user;
    }
}
