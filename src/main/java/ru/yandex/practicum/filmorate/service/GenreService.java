package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(Long genreId) {
        return genreDbStorage.getGenreById(genreId);
    }
}
