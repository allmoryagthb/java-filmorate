package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        log.info("Вернуть всех пользователей");
        return userStorage.getAllUsers();
    }

    public User addNewUser(@Valid User user) {
        userValidator(user);
        userStorage.addNewUser(user);
        log.info("Добавлен новый пользователь с id = {}", user.getId());
        return user;
    }

    public User updateUser(@Valid User user) {
        checkId(user.getId());
        userValidator(user);
        User updUser = userStorage.updateUser(user);
        log.info("Пользователь с id = {} успешно обновлен", updUser.getId());
        return updUser;
    }

    public void addUserToFriend(Long userId, Long friendId) {
        checkId(userId);
        checkId(friendId);
        checkIfIdsAreEquals(userId, friendId);
        userStorage.addUserToFriend(userId, friendId);
    }

    public void removeUserFromFriends(Long userId, Long friendId) {
        checkId(userId);
        checkId(friendId);
        checkIfIdsAreEquals(userId, friendId);
        userStorage.removeUserFromFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        checkId(userId);
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        checkId(userId);
        checkId(otherId);
        checkIfIdsAreEquals(userId, otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }

    private void userValidator(@Valid User user) {
        if (user.getLogin().contains(" ")) {
            log.error("invalid login - validation failed");
            throw new ValidationException("invalid login");
        }
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            log.info("user name is empty - use login credentials as user name");
            user.setName(user.getLogin());
        }
    }

    private void checkId(Long userId) {
        if (Objects.isNull(userId))
            throw new ValidationException("Некорректный id");
        if (!userStorage.checkUserExistsById(userId))
            throw new EntityNotFoundException("Пользователя с id = %s не существует".formatted(userId));
    }

    private void checkIfIdsAreEquals(Long userId, Long friendId) {
        if (userId.equals(friendId))
            throw new ValidationException("Переданы одинаковые id пользователей");
    }
}
