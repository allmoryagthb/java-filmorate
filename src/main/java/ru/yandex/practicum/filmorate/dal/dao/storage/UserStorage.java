package ru.yandex.practicum.filmorate.dal.dao.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    void addNewUser(User user);

    User updateUser(User user);

    boolean checkUserExistsById(Long userId);

    void addUserToFriend(Long userId, Long friendId);

    void removeUserFromFriends(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long otherId);
}
