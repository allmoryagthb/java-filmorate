package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public ResponseEntity<Map<Long, User>> getUsers() {
        if (users.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        if (users.containsKey(user.getId()))
            throw new ValidationException("Пользователь с таким id уже существует");
        try {
            userValidator(user);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь не прошел валидацию");
        }
        this.users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователя с таким id не существует");
        try {
            userValidator(user);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь не прошел валидацию");
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok("Данные пользователя с id %s успешно обновлены".formatted(user.getId()));
    }

    private void userValidator(User user) {
        if (Objects.isNull(user.getId()) ||
                Objects.isNull(user.getEmail()) ||
                Objects.isNull(user.getLogin()) ||
                Objects.isNull(user.getBirthday()) ||
                !user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") ||
                user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Пользователь не прошел валидацию");
        if (Objects.isNull(user.getName()) || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
