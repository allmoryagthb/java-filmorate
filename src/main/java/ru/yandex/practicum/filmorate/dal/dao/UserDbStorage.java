package ru.yandex.practicum.filmorate.dal.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.storage.UserStorage;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    protected JdbcTemplate jdbcTemplate;
    private static final String GET_USERS = """
            SELECT *
            FROM users
            ORDER BY users.id ASC
            """;
    private static final String GET_USER_BY_ID = """
            SELECT *
            FROM users
            WHERE id = ?
            """;
    private static final String GET_USER_FRIENDS = """
            SELECT *
            FROM users
            WHERE users.id IN (SELECT friends.friend_id
                               FROM friends
                               WHERE friends.id = ?
                               )
            ORDER BY id
            """;
    private static final String GET_USER_COUNT_BY_ID = """
            SELECT COUNT(*)
            FROM users
            WHERE users.id = ?
            """;
    private static final String ADD_NEW_USER = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?, ?, ?, ?)
            """;
    private static final String UPDATE_USER = """
            UPDATE users
            SET email = ?,
                login = ?,
                name = ?,
                birthday = ?
            WHERE id = ?
            """;
    private static final String CHECK_FRIENDSHIP_REQUEST_EXISTS = """
            SELECT COUNT(*)
            FROM friends
            WHERE friends.id = ? AND friends.friend_id = ?
            """;
    private static final String CHECK_FRIENDSHIP_APPROVED = """
            SELECT status
            FROM friends
            WHERE friends.id = ? AND friends.friend_id = ?
            """;
    private static final String ADD_FRIENDSHIP_REQUEST = """
            INSERT INTO friends (id, friend_id, status)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_FRIENDSHIP_STATUS_ONE_USER = """
            UPDATE friends
            SET status = ?
            WHERE id = ? AND friend_id = ?
            """;
    private static final String UPDATE_FRIENDSHIP_STATUS_BOTH_USERS = """
            UPDATE friends
            SET status = ?
            WHERE id IN (?, ?) AND friend_id IN (?, ?)
            """;
    private static final String DELETE_FRIENDSHIP = """
            DELETE FROM friends
            WHERE id = ? AND friend_id = ?
            """;
    private static final String GET_FRIENDSHIP_STATUS = """
            SELECT status
            FROM friends
            WHERE id = ? AND friend_id = ?
            """;

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query(GET_USERS, new UserRowMapper(jdbcTemplate));
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_USER_BY_ID, new UserRowMapper(jdbcTemplate), id));
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
    public UserDto updateUser(User user) {
        UserDto userDto = UserMapper.jpaToDto(user);
        jdbcTemplate.update(
                UPDATE_USER,
                userDto.getEmail(),
                userDto.getName(),
                userDto.getBirthday());
        return userDto;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        return jdbcTemplate.queryForObject(GET_USER_COUNT_BY_ID, Long.class, userId) == 1;
    }

    @Override
    public void addUserToFriend(Long userId, Long friendId) {
        if (!checkFriendshipRequestExists(userId, friendId)) {
            jdbcTemplate.update(ADD_FRIENDSHIP_REQUEST, userId, friendId, false);
            if (checkFriendshipRequestExists(friendId, userId)) {
                jdbcTemplate.update(UPDATE_FRIENDSHIP_STATUS_BOTH_USERS, true, userId, friendId, userId, friendId);
            }
        } else {
            throw new ValidationException("Пользователь уже добавлен в друзья");
        }
    }

    @Override
    public void removeUserFromFriends(Long userId, Long friendId) {
        jdbcTemplate.update(DELETE_FRIENDSHIP, userId, friendId);
        if (checkFriendshipRequestExists(friendId, userId))
            jdbcTemplate.update(UPDATE_FRIENDSHIP_STATUS_ONE_USER, false, friendId, userId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return jdbcTemplate.query(GET_USER_FRIENDS, new UserRowMapper(jdbcTemplate), userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        Collection<User> userFriends = getFriends(userId);
        userFriends.retainAll(getFriends(friendId));
        return userFriends;
    }

    public Boolean getFriendshipStatus(Long userId, Long friendId) {
        if (checkFriendshipRequestExists(userId, friendId))
            return jdbcTemplate.queryForObject(GET_FRIENDSHIP_STATUS, Boolean.class, userId, friendId);
        throw new EntityNotFoundException("404 Not found");
    }

    private boolean checkFriendshipRequestExists(Long userId, Long friendId) {
        return jdbcTemplate.queryForObject(CHECK_FRIENDSHIP_REQUEST_EXISTS, Long.class, userId, friendId) == 1;
    }

    private boolean checkFriendshipApproved(Long userId, Long friendId) {
        return jdbcTemplate.queryForObject(CHECK_FRIENDSHIP_APPROVED, Boolean.class, userId, friendId);
    }
}
