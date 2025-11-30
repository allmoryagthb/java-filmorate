package ru.yandex.practicum.filmorate.dal.mappers;

import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {
    public static Film dtoToJpa(FilmDto objectDto) {
        return Film.builder()
                .id(objectDto.getId())
                .name(objectDto.getName())
                .description(objectDto.getDescription())
                .releaseDate(objectDto.getReleaseDate())
                .duration(objectDto.getDuration())
                .build();
    }

    public static FilmDto jpaToDto(Film objectJpa) {
        return FilmDto.builder()
                .id(objectJpa.getId())
                .name(objectJpa.getName())
                .description(objectJpa.getDescription())
                .releaseDate(objectJpa.getReleaseDate())
                .duration(objectJpa.getDuration())
                .build();
    }
}
