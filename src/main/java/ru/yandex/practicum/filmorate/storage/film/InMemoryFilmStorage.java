package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film entity) {
        entity.setId(getNextId());
        films.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Film update(Film entity) {
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
    public Collection<Film> findAll() {
        return films.values();
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
