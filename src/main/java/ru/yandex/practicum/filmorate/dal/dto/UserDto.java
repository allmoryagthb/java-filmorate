package ru.yandex.practicum.filmorate.dal.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private LocalDate birthday;
}
