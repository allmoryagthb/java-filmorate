package ru.yandex.practicum.filmorate.dal.dao.storage;

import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void addNewUser(User user);

    UserDto updateUser(User user);

    boolean checkUserExistsById(Long userId);

    void addUserToFriend(Long userId, Long friendId);

    void removeUserFromFriends(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long otherId);
}
