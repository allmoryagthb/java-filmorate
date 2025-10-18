package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public void addNewUser(@RequestBody User user) {
        userValidator(user);
        user.setId(++id);
        users.put(user.getId(), user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        if (Objects.isNull(user.getId()) || !users.containsKey(user.getId()))
            throw new CustomValidationException("incorrect id");
        userValidator(user);
        users.put(user.getId(), user);
    }

    private void userValidator(User user) {
        if (Objects.isNull(user.getEmail())) {
            throw new CustomValidationException("email is null");
        }
        if (Objects.isNull(user.getLogin())) {
            throw new CustomValidationException("login is null");
        }
        if (Objects.isNull(user.getBirthday())) {
            throw new CustomValidationException("birthday is null");
        }
        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new CustomValidationException("invalid email");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new CustomValidationException("invalid login");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new CustomValidationException("invalid birthday");
        }

        if (Objects.isNull(user.getName()) || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
