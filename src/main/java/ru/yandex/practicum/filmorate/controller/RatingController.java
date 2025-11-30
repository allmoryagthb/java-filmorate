package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Rating getGenreById(@PathVariable(value = "id") Long ratingId) {
        return ratingService.getRatingById(ratingId);
    }
}
