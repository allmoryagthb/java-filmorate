package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User addNewUser(User user) {
        user.setId(++id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public void addUserToFriend(Long userId, Long friendId) {
        users.get(userId).getFriendsList().add(friendId);
        users.get(friendId).getFriendsList().add(userId);
    }

    @Override
    public void removeUserFromFriends(Long userId, Long friendId) {
        users.get(userId).getFriendsList().remove(friendId);
        users.get(friendId).getFriendsList().remove(userId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return users.get(userId).getFriendsList()
                .stream()
                .map(users::get)
                .toList();
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> intersections = users.get(userId).getFriendsList();
        intersections.retainAll(users.get(otherId).getFriendsList());
        return intersections
                .stream()
                .map(users::get)
                .toList();
    }
}
