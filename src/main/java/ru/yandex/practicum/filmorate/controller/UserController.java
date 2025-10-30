package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping
    public User addNewUser(@RequestBody @Valid User user) {
        return userStorage.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userStorage.updateUser(user);
    }

}
