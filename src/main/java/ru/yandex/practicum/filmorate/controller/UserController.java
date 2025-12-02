package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("Получить список всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable(value = "userId") Long userId) {
        log.info("Получить пользователя с id = '{}'", userId);
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable(value = "userId") Long userId) {
        log.info("Получить список друзей пользователя с id = '{}'", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "otherId") Long otherId) {
        log.info("Получить список общих друзей: userId = {}, otherId = {}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewUser(@RequestBody @Valid User user) {
        log.info("Добавить нового пользователя");
        return userService.addNewUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Обновить пользователя");
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addUserToFriend(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "friendId") Long friendId) {
        log.info("Добавить пользователя в друзья: userId = {}, friendId = {}", userId, friendId);
        userService.addUserToFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeUserFromFriends(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "friendId") Long friendId) {
        log.info("Удалить пользователя из друзей: userId = {}, friendId = {}", userId, friendId);
        userService.removeUserFromFriends(userId, friendId);
    }
}
