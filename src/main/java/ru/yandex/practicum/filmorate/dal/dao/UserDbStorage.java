package ru.yandex.practicum.filmorate.dal.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {
    @Override
    public Collection<User> getAllUsers() {
        return List.of();
    }

    @Override
    public User addNewUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        return false;
    }

    @Override
    public void addUserToFriend(Long userId, Long friendId) {

    }

    @Override
    public void removeUserFromFriends(Long userId, Long friendId) {

    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return List.of();
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        return List.of();
    }
}
