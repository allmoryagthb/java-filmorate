package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class RatingService {
    private final RatingDbStorage ratingDbStorage;

    public Collection<Rating> getAllRatings() {
        return ratingDbStorage.getAllRatings();
    }

    public Rating getRatingById(Long ratingId) {
        return ratingDbStorage.getRatingById(ratingId);
    }
}
