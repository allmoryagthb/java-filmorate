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

    private Long id = 0L;


    public Collection<User> getAllUsers() {
        log.info("Вернуть всех пользователей");
        return userStorage.getAllUsers();
    }

    public User addNewUser(@Valid User user) {
        userValidator(user);
        user.setId(++id);
        userStorage.addNewUser(user);
        log.info("Добавлен новый пользователь с id = {}", id);
        return user;
    }

    public User updateUser(@Valid User user) {
        if (Objects.isNull(user.getId()))
            throw new ValidationException("Некорректный id");
        if (!userStorage.checkUserExistsById(user.getId()))
            throw new EntityNotFoundException("Пользователя с id = %s не существует".formatted(user.getId()));
        userValidator(user);
        User updUser = userStorage.updateUser(user);
        log.info("Пользователь с id = {} успешно обновлен", updUser.getId());
        return updUser;
    }

    public void addUserToFriend(Long userId, Long friendId) {
        storagesValidator(userId, friendId);
        userStorage.addUserToFriend(userId, friendId);
    }

    public void removeUserFromFriends(Long userId, Long friendId) {
        storagesValidator(userId, friendId);
        userStorage.removeUserFromFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        if (userId == null)
            throw new ValidationException("Переданы некорректные параметры");
        if (!userStorage.checkUserExistsById(userId))
            throw new EntityNotFoundException("Пользователя с id = %s не существует".formatted(userId));
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        storagesValidator(userId, otherId);
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

    private void storagesValidator(Long userId, Long friendId) {
        if (userId == null || friendId == null)
            throw new ValidationException("Переданы некорректные параметры");
        if (userId.equals(friendId))
            throw new ValidationException("Переданы одинаковые id пользователей");
        if (!userStorage.checkUserExistsById(userId))
            throw new EntityNotFoundException("Пользователя с id = %s не существует".formatted(userId));
        if (!userStorage.checkUserExistsById(friendId))
            throw new EntityNotFoundException("Пользователя с id = %s не существует".formatted(friendId));
    }


}
