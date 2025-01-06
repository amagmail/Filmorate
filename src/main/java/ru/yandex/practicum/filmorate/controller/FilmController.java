package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        // Название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Поле name содержит невалидное значение");
        }
        // Максимальная длина описания — 200 символов
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Максимальная длина описания не должна превышать 200 символов");
            throw new ValidationException("Поле description содержит невалидное значение");
        }
        // Дата релиза — не раньше 28 декабря 1895 года
        LocalDate date = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.from(date))) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Поле releaseDate содержит невалидное значение");
        }
        // Продолжительность фильма должна быть положительным числом
        if (film.getDuration() != null && film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Поле duration содержит невалидное значение");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Идентификатор фильма не может быть пустым");
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) {
                if (newFilm.getDescription().length() > 200) {
                    log.error("Максимальная длина описания не должна превышать 200 символов");
                    throw new ValidationException("Поле description содержит невалидное значение");
                } else {
                    oldFilm.setDescription(newFilm.getDescription());
                }
            }
            if (newFilm.getReleaseDate() != null) {
                LocalDate date = LocalDate.of(1895, 12, 28);
                if (newFilm.getReleaseDate().isBefore(LocalDate.from(date))) {
                    log.error("Дата релиза не может быть раньше 28 декабря 1895 года");
                    throw new ValidationException("Поле releaseDate содержит невалидное значение");
                } else {
                    oldFilm.setReleaseDate(newFilm.getReleaseDate());
                }
            }
            if (newFilm.getDuration() != null) {
                if (newFilm.getDuration() > 0) {
                    oldFilm.setDuration(newFilm.getDuration());
                } else {
                    log.error("Продолжительность фильма должна быть положительным числом");
                    throw new ValidationException("Поле duration содержит невалидное значение");
                }
            }
            return oldFilm;
        }
        log.error("Фильм с идентификатором " + newFilm.getId() + " не найден");
        throw new NotFoundException("Фильм с идентификатором " + newFilm.getId() + " не найден");
    }

    // Вспомогательный метод для генерации идентификатора объекта
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
