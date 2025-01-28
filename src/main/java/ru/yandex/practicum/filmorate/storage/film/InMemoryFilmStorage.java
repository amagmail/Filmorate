package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Set<Long> setLike(Long filmId, Long userId) {
        Film film = getItem(filmId);
        film.getLikes().add(userId);
        return film.getLikes();
    }

    @Override
    public Set<Long> removeLike(Long filmId, Long userId) {
        Film film = getItem(filmId);
        film.getLikes().remove(userId);
        return film.getLikes();
    }

    @Override
    public List<Film> getPopular(int count) {
        return getItems().stream()
                .sorted(Film::compareLikes)
                .limit(count)
                .toList();
    }

    @Override
    public Film create(Film entity) {
        entity.setId(getNextId());
        films.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Film update(Film entity) {
        if (entity.getId() == null) {
            log.error("Идентификатор фильма не может быть пустым");
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        if (films.containsKey(entity.getId())) {
            Film oldFilm = films.get(entity.getId());
            if (entity.getName() != null && !entity.getName().isBlank()) {
                oldFilm.setName(entity.getName());
            }
            if (entity.getDescription() != null && !entity.getDescription().isBlank()) {
                oldFilm.setDescription(entity.getDescription());
            }
            if (entity.getDuration() != null) {
                oldFilm.setDuration(entity.getDuration());
            }
            if (entity.getReleaseDate() != null) {
                oldFilm.setReleaseDate(entity.getReleaseDate());
            }
            return oldFilm;
        }
        log.error("Фильм с идентификатором " + entity.getId() + " не найден");
        throw new NotFoundException("Фильм с идентификатором " + entity.getId() + " не найден");
    }

    @Override
    public Collection<Film> getItems() {
        return films.values();
    }

    @Override
    public Film getItem(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильма с идентификатором " + filmId + " не существует");
            throw new NotFoundException("Фильма с идентификатором " + filmId + " не существует");
        }
        return films.get(filmId);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
