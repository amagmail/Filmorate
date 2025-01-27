package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface Storage<T> {

    T create(T entity);

    T update(T entity);

    T getItem(Long id);

    Collection<T> getItems();

}
