package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("get all users");
        return users.values();
    }

    @PostMapping
    public User addNewUser(@RequestBody @Valid User user) {
        userValidator(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("new user successfully added");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (Objects.isNull(user.getId()) || !users.containsKey(user.getId()))
            throw new CustomValidationException("incorrect id");
        userValidator(user);
        users.put(user.getId(), user);
        log.info("user updated");
        return user;
    }

    private void userValidator(@Valid User user) {
        if (user.getLogin().contains(" ")) {
            log.error("invalid login - validation failed");
            throw new CustomValidationException("invalid login");
        }
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            log.info("user name is empty - use login credentials as user name");
            user.setName(user.getLogin());
        }
    }
}
