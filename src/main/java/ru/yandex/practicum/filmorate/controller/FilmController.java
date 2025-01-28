package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.yandex.practicum.filmorate.service.FilmService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PutMapping("/{filmId}/like/{userId}")
    public Set<Long> setLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        /*
        Film film = filmService.getItem(filmId);
        film.getLikes().add(userId);
        return film.getLikes();*/
        return filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Set<Long> removeLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        /*
        Film film = filmService.getItem(filmId);
        film.getLikes().remove(userId);
        return film.getLikes();*/
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") int count) {
        /*
        Collection<Film> films = filmService.getItems();
        return films.stream()
                .sorted(Film::compareLikes)
                .limit(count)
                .toList();*/
        return filmService.getPopular(count);
    }

    @GetMapping("/{filmId}")
    public Film getItem(@PathVariable("filmId") Long filmId) {
        return filmService.getItem(filmId);
    }

    @GetMapping
    public Collection<Film> getItems() {
        return filmService.getItems();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        // Название не может быть пустым: @NotNull, @NotBlank
        // Максимальная длина описания — 200 символов: @Size(max = 200)
        // Продолжительность фильма должна быть положительным числом: @Positive
        // Дата релиза — не раньше 28 декабря 1895 года

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Идентификатор фильма не может быть пустым");
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        return filmService.update(newFilm);
    }
}
