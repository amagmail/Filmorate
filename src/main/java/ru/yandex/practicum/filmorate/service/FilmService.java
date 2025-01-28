package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public Set<Long> setLike(Long filmId, Long userId) {
        return filmStorage.setLike(filmId, userId);
    }

    public Set<Long> removeLike(Long filmId, Long userId) {
        return filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getItems() {
        return filmStorage.getItems();
    }

    public Film getItem(Long filmId) {
        return filmStorage.getItem(filmId);
    }

}
