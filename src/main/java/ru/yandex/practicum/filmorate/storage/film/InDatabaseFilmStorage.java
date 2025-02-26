package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Primary
@Component
public class InDatabaseFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;

    private static final String GET_ITEMS = "select *, (select count(user_id) from likes where film_id = id) as likes from films";
    private static final String GET_ITEM = "select *, (select count(user_id) from likes where film_id = id) as likes from films where id = ?";
    private static final String INSERT_QUERY = "insert into films(name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa = ? where id = ?";
    private static final String SET_LIKE = "insert into likes(film_id, user_id) values(?, ?)";
    private static final String REMOVE_LIKE = "delete from likes where film_id = ? and user_id = ?";
    private static final String GET_LIKES = "select user_id from likes where film_id = ?";
    private static final String GET_POPULAR = "select t1.*, count(t2.user_id) as likes " +
            "from films t1 " +
            "left join likes t2 on t2.film_id = t1.id " +
            "group by t1.id " +
            "order by likes desc " +
            "limit ?";

    private static final String SET_GENRE = "insert into film_genre(film_id, genre_id) values(?, ?)";
    private static final String REMOVE_GENRES = "delete from film_genre where film_id = ?";
    private static final String GET_GENRES = "select genre_id from film_genre where film_id = ?";

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
            ps.setObject(5, entity.getMpa());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            entity.setId(id);
            for (Long genreId : entity.getGenre()) {
                jdbc.update(SET_GENRE, id, genreId);
            }
            return entity;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public Film update(Film entity) {
        int rowsUpdated = jdbc.update(UPDATE_QUERY, entity.getName(), entity.getDescription(), entity.getReleaseDate(), entity.getDuration(), entity.getMpa(), entity.getId());
        if (rowsUpdated > 0) {
            jdbc.update(REMOVE_GENRES, entity.getId());
            for (Long genreId : entity.getGenre()) {
                jdbc.update(SET_GENRE, entity.getId(), genreId);
            }
            return entity;
        } else {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public Collection<Film> getItems() {
        Collection<Film> items = jdbc.query(GET_ITEMS, mapper);
        for (Film film : items) {
            List<Long> genreIds = jdbc.queryForList(GET_GENRES, Long.class, film.getId());
            film.setGenre(new HashSet<>(genreIds));
        }
        return items;
    }

    @Override
    public Film getItem(Long filmId) {
        /*
        Film film = jdbc.queryForObject(GET_ITEM, mapper, filmId);
        if (film != null) {
            Set<Long> likes = getLikes(filmId);
            film.setLikes(likes);
        } */
        Film film = jdbc.queryForObject(GET_ITEM, mapper, filmId);
        if (film != null) {
            List<Long> genreIds = jdbc.queryForList(GET_GENRES, Long.class, filmId);
            film.setGenre(new HashSet<>(genreIds));
        }
        return film;
    }

    @Override
    public Set<Long> setLike(Long filmId, Long userId) {
        Set<Long> likes = getLikes(filmId);
        if (!likes.contains(userId)) {
            int rowsUpdated = jdbc.update(SET_LIKE, filmId, userId);
            if (rowsUpdated > 0) {
                likes.add(userId);
            }
        }
        return likes;
    }

    @Override
    public Set<Long> removeLike(Long filmId, Long userId) {
        jdbc.update(REMOVE_LIKE, filmId, userId);
        return getLikes(filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        Collection<Film> items = jdbc.query(GET_POPULAR, mapper, count);
        for (Film film : items) {
            List<Long> genreIds = jdbc.queryForList(GET_GENRES, Long.class, film.getId());
            film.setGenre(new HashSet<>(genreIds));
        }
        return items;
    }

    public Set<Long> getLikes(Long filmId) {
        List<Long> checkVals = checkItemsQuery(List.of(filmId));
        if (checkVals.isEmpty()) {
            throw new NotFoundException("Не удалось найти фильм");
        }
        List<Long> userIds = jdbc.queryForList(GET_LIKES, Long.class, filmId);
        return new HashSet<>(userIds);
    }

    public List<Long> checkItemsQuery(List<Long> ids) {
        String query = "select id from films where id in (";
        query += String.join(",", Collections.nCopies(ids.size(), "?"));
        query += ")";
        return jdbc.queryForList(query, Long.class, ids.toArray());
    }

    /*
    public List<Long> setGenresQuery(Long filmId, Set<Long> genreIds) {
        String query = "insert into film_genre(film_id, genre_id) values";
        query += String.join(",", Collections.nCopies(genreIds.size(), "(" + filmId + ",?)"));
        int res = jdbc.update(query, genreIds);
        return null;
    } */

}
