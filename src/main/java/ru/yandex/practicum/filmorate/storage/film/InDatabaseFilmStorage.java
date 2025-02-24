package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Set;

@Primary
@Component
public class InDatabaseFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;

    private static final String GET_ITEMS = "select * from films";
    private static final String GET_ITEM = "select * from films where id = ?";
    private static final String INSERT_QUERY = "insert into films(name, description, release_date, duration, genre, mpa) values (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update films set name = ?, description = ?, release_date = ?, duration = ?, genre = ?, mpa = ? where id = ?";
    private static final String SET_LIKE = "insert into likes(film_id, user_id) values(?, ?)";
    private static final String REMOVE_LIKE = "delete from likes WHERE film_id = ? and user_id = ?";

    public InDatabaseFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Film create(Film entity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, entity.getName());
            ps.setObject(2, entity.getDescription());
            ps.setObject(3, entity.getReleaseDate());
            ps.setObject(4, entity.getDuration());
            ps.setObject(5, entity.getGenre());
            ps.setObject(6, entity.getMpa());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            entity.setId(id);
            return entity;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public Film update(Film entity) {
        int rowsUpdated = jdbc.update(UPDATE_QUERY, entity.getName(), entity.getDescription(), entity.getReleaseDate(), entity.getDuration(), entity.getGenre(), entity.getMpa(), entity.getId());
        if (rowsUpdated > 0) {
            return entity;
        } else {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public Collection<Film> getItems() {
        return jdbc.query(GET_ITEMS, mapper);
    }

    @Override
    public Film getItem(Long filmId) {
        return jdbc.queryForObject(GET_ITEM, mapper, filmId);
    }

    @Override
    public Set<Long> setLike(Long filmId, Long userId) {
        //jdbc.update(SET_LIKE, filmId, userId);
        return null;
    }

    @Override
    public Set<Long> removeLike(Long filmId, Long userId) {
        //jdbc.update(REMOVE_LIKE, filmId, userId);
        return null;
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return null;
    }

}
