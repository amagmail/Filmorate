package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.yandex.practicum.filmorate.service.FilmService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PutMapping("/{filmId}/like/{userId}")
    public Set<Long> setLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Set<Long> removeLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") int count) {
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
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        // Название не может быть пустым: @NotNull, @NotBlank
        // Максимальная длина описания — 200 символов: @Size(max = 200)
        // Продолжительность фильма должна быть положительным числом: @Positive
        // Дата релиза — не раньше 28 декабря 1895 года
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        return new ErrorResponse(
                "Ошибка валидации",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return new ErrorResponse(
                "Искомый объект не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final RuntimeException e) {
        return new ErrorResponse(
                "Возникло исключение",
                e.getMessage()
        );
    }
}
