package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;


    @Override
    public Collection<User> getAllUsers() {
        log.info("get all users");
        return users.values();
    }

    @Override
    public User addNewUser(User user) {
        userValidator(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("new user successfully added");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (Objects.isNull(user.getId()) || !users.containsKey(user.getId()))
            throw new ValidationException("incorrect id");
        userValidator(user);
        users.put(user.getId(), user);
        log.info("user updated");
        return user;
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        return users.containsKey(userId);
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
}
