package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class RatingService {
    private final MpaDbStorage ratingDbStorage;

    public Collection<Mpa> getAllRatings() {
        log.info("Сервис - Получить список с всеми рейтингами");
        return ratingDbStorage.getAllMpas();
    }

    public Mpa getRatingById(Long ratingId) {
        log.info("Сервис - Получить рейтинг с id = {}", ratingId);
        return ratingDbStorage.getMpaById(ratingId)
                .orElseThrow(() -> new EntityNotFoundException("Рейтинг с id = '%d' не найден".formatted(ratingId)));
    }
}
