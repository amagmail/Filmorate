package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import ru.yandex.practicum.filmorate.service.FilmService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
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
