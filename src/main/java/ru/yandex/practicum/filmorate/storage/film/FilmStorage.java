package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Set;

public interface FilmStorage extends Storage<Film> {

    Set<Long> setLike(Long filmId, Long userId);

    Set<Long> removeLike(Long filmId, Long userId);

    List<Film> getPopular(int count);

}
