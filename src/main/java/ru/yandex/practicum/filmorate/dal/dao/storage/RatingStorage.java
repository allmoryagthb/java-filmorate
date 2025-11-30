package ru.yandex.practicum.filmorate.dal.dao.storage;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

public interface RatingStorage {
    Collection<Rating> getAllRatings();

    Rating getRatingById(Long id);
}
