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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Primary
@Component
public class InDatabaseUserStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    private static final String GET_ITEMS = "select *, (select count(friend_id) from friendship where user_id = id) as friends from users";
    private static final String GET_ITEM = "select *, (select count(friend_id) from friendship where user_id = id) as friends from users where id = ?";
    private static final String INSERT_QUERY = "insert into users(name, email, login, birthday) values (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update users set name = ?, email = ?, login = ?, birthday = ? where id = ?";

    private static final String SET_FRIEND = "insert into friendship(user_id, friend_id) values(?, ?)";
    private static final String REMOVE_FRIEND = "delete from friendship where user_id = ? and friend_id = ?";
    private static final String GET_FRIENDS = "select friend_id from friendship where user_id = ?";

    private static final String GET_USER_FRIENDS = "select t2.*, (select count(friend_id) from friendship where user_id = t2.id) as friends " +
            "from friendship t1 " +
            "inner join users t2 on t2.id = t1.friend_id " +
            "where t1.user_id = ?";

    private static final String GET_MUTUAL_FRIENDS = "select *, (select count(friend_id) from friendship where user_id = id) as friends " +
            "from users " +
            "where id in (select friend_id from friendship where user_id = ?) " +
            "and id in (select friend_id from friendship where user_id = ?)";

    public InDatabaseUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public User create(User entity) {
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName(entity.getLogin());
        }
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
        Set<Long> friends = getFriends(userId);
        if (!friends.contains(friendId)) {
            int rowsUpdated = jdbc.update(SET_FRIEND, userId, friendId);
            if (rowsUpdated > 0) {
                friends.add(friendId);
            }
        }
        return friends;
    }

    @Override
    public Set<Long> removeFriend(Long userId, Long friendId) {
        jdbc.update(REMOVE_FRIEND, userId, friendId);
        return getFriends(userId);
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        return jdbc.query(GET_USER_FRIENDS, mapper, userId);
    }

    @Override
    public Collection<User> getMutualFriends(Long userId, Long otherId) {
        return jdbc.query(GET_MUTUAL_FRIENDS, mapper, userId, otherId);
    }

    public Set<Long> getFriends(Long userId) {
        List<Long> userIds = jdbc.queryForList(GET_FRIENDS, Long.class, userId);
        return new HashSet<>(userIds);
    }

}
