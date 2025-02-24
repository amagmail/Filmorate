package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Set;

@Primary
@Component
public class InDatabaseUserStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    private static final String GET_ITEMS = "select * from users";
    private static final String GET_ITEM = "select * from users where id = ?";
    private static final String INSERT_QUERY = "insert into users(name, email, login, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update users set name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";

    public InDatabaseUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public User create(User entity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, entity.getName());
            ps.setObject(2, entity.getLogin());
            ps.setObject(3, entity.getEmail());
            ps.setObject(4, entity.getBirthday());
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
    public User update(User entity) {
        int rowsUpdated = jdbc.update(UPDATE_QUERY, entity.getName(), entity.getLogin(), entity.getEmail(), entity.getBirthday(), entity.getId());
        if (rowsUpdated > 0) {
            return entity;
        } else {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public Collection<User> getItems() {
        return jdbc.query(GET_ITEMS, mapper);
    }

    @Override
    public User getItem(Long userId) {
        return jdbc.queryForObject(GET_ITEM, mapper, userId);
    }

    @Override
    public Set<Long> setFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public Set<Long> removeFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        return null;
    }

    @Override
    public Collection<User> getMutualFriends(Long userId, Long otherId) {
        return null;
    }

}
