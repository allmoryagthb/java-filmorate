package ru.yandex.practicum.filmorate.dal.mappers;

import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static User dtoToJpa(UserDto objectDto) {
        return User.builder()
                .id(objectDto.getId())
                .email(objectDto.getEmail())
                .name(objectDto.getName())
                .birthday(objectDto.getBirthday())
                .build();
    }

    public static UserDto jpaToDto(User objectJpa) {
        return UserDto.builder()
                .id(objectJpa.getId())
                .email(objectJpa.getEmail())
                .name(objectJpa.getName())
                .birthday(objectJpa.getBirthday())
                .build();
    }
}
