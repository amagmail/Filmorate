package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User entity) {
        entity.setId(getNextId());
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User update(User entity) {
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
    public Collection<User> findAll() {
        return users.values();
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
