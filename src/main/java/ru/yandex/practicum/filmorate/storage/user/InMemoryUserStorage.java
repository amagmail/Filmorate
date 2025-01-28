package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Set<Long> setFriend(Long userId, Long friendId) {
        User user1 = getItem(userId);
        User user2 = getItem(friendId);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        return user1.getFriends();
    }

    @Override
    public Set<Long> removeFriend(Long userId, Long friendId) {
        User user1 = getItem(userId);
        User user2 = getItem(friendId);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        return user1.getFriends();
    }

    @Override
    public Set<Long> getUserFriends(Long userId) {
        User user = getItem(userId);
        return user.getFriends();
    }

    @Override
    public Set<Long> getMotualFriends(Long userId, Long otherId) {
        User user1 = getItem(userId);
        User user2 = getItem(otherId);
        Set<Long> mutualFriends = new HashSet<>(user1.getFriends());
        mutualFriends.retainAll(user2.getFriends());
        return mutualFriends;
    }

    @Override
    public User create(User entity) {
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName(entity.getLogin());
        }
        entity.setId(getNextId());
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User update(User entity) {
        if (entity.getId() == null) {
            log.error("Идентификатор пользователя не может быть пустым");
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        if (users.containsKey(entity.getId())) {
            User oldUser = users.get(entity.getId());
            if (entity.getEmail() != null && !entity.getEmail().isBlank()) {
                oldUser.setEmail(entity.getEmail());
            }
            if (entity.getLogin() != null && !entity.getLogin().isBlank()) {
                oldUser.setLogin(entity.getLogin());
            }
            if (entity.getName() != null && !entity.getName().isBlank()) {
                oldUser.setName(entity.getName());
            }
            if (entity.getBirthday() != null) {
                oldUser.setBirthday(entity.getBirthday());
            }
            return oldUser;
        }
        log.error("Пользователь с идентификатором " + entity.getId() + " не найден");
        throw new NotFoundException("Пользователь с идентификатором " + entity.getId() + " не найден");
    }

    @Override
    public Collection<User> getItems() {
        return users.values();
    }

    @Override
    public User getItem(Long userId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователя с идентификатором " + userId + " не существует");
            throw new ValidationException("Пользователя с идентификатором " + userId + " не существует");
        }
        return users.get(userId);
    }

    // Вспомогательный метод для генерации идентификатора объекта
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
