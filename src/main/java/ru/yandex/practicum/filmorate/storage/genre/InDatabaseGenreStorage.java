package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class InDatabaseGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;

    private static final String GET_ITEMS = "select * from genres";
    private static final String GET_ITEM = "select * from genres where id = ?";

    public InDatabaseGenreStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Genre create(Genre entity) {
        return null;
    }

    @Override
    public Genre update(Genre entity) {
        return null;
    }

    @Override
    public Collection<Genre> getItems() {
        return jdbc.query(GET_ITEMS, mapper);
    }

    @Override
    public Genre getItem(Long id) {
        List<Long> checkVals = checkItemsQuery(List.of(id));
        if (checkVals.isEmpty()) {
            throw new NotFoundException("Не удалось найти жанр");
        }
        return jdbc.queryForObject(GET_ITEM, mapper, id);
    }

    public List<Long> checkItemsQuery(List<Long> ids) {
        String query = "select id from genres where id in (";
        query += String.join(",", Collections.nCopies(ids.size(), "?"));
        query += ")";
        return jdbc.queryForList(query, Long.class, ids.toArray());
    }

}
