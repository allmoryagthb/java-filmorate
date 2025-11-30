package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class RatingService {
    private final MpaDbStorage ratingDbStorage;

    public Collection<Mpa> getAllRatings() {
        return ratingDbStorage.getAllMpas();
    }

    public Mpa getRatingById(Long ratingId) {
        return ratingDbStorage.getMpaById(ratingId);
    }
}
