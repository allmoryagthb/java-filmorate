package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.UserStorage;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    protected JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final static String GET_USERS = """
            SELECT *
            FROM users
            ORDER BY users.id ASC
            """;
    private final static String GET_USER_FRIENDS = """
            SELECT *
            FROM users
            INNER JOIN friends ON users.id = friends.id
            WHERE users.id = ?
            ORDER BY friends.id
            """;
    private final static String GET_USER_COUNT_BY_ID = """
            SELECT COUNT(*)
            FROM users
            WHERE users.id = ?
            """;
    private final static String ADD_NEW_USER = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?, ?, ?, ?)
            """;
    private final static String CHECK_FRIENDSHIP_REQUEST_EXISTS = """
            SELECT COUNT(*)
            FROM friends
            WHERE friends.id = ? AND friends.friend_id = ?
            """;
    private final static String CHECK_FRIENDSHIP_APPROVED = """
            SELECT status
            FROM friends
            WHERE friends.id = ? AND friends.friend_id = ?
            """;
    private final static String ADD_FRIENDSHIP_REQUEST = """
            INSERT INTO friends(id, friend_id, status)
            VALUES (?, ?,?)
            """;
    private final static String UPDATE_FRIENDSHIP_STATUS = """
            
            """;

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query(GET_USERS, userRowMapper);
    }

    @Override
    public void addNewUser(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(ADD_NEW_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getEmail());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        // Возвращаем id нового пользователя
        if (id != null) {
            user.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        return jdbcTemplate.queryForObject(GET_USER_COUNT_BY_ID, Long.class, userId) == 1;
    }

    @Override
    public void addUserToFriend(Long userId, Long friendId) {
        if (!checkFriendshipRequestExists(userId, friendId)) {
            jdbcTemplate.update(ADD_FRIENDSHIP_REQUEST, userId, friendId, false);
        }
        throw new ValidationException("Пользователь уже добавлен в друзья");
    }

    @Override
    public void removeUserFromFriends(Long userId, Long friendId) {

    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return jdbcTemplate.query(GET_USER_FRIENDS, userRowMapper, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        return List.of();
    }

    public boolean checkFriendshipRequestExists(Long userId, Long friendId) {
        return jdbcTemplate.queryForObject(CHECK_FRIENDSHIP_REQUEST_EXISTS, Long.class, userId, friendId) == 1;
    }

    public boolean checkFriendshipApproved(Long userId, Long friendId) {
        return jdbcTemplate.queryForObject(CHECK_FRIENDSHIP_APPROVED, Boolean.class, userId, friendId);
    }
}
