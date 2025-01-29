package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    public Set<Long> setFriend(Long userId, Long friendId) {
        return userStorage.setFriend(userId, friendId);
    }

    public Set<Long> removeFriend(Long userId, Long friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public Set<Long> getUserFriends(Long userId) {
        return userStorage.getUserFriends(userId);
    }

    public Set<Long> getMotualFriends(Long userId, Long otherId) {
        return userStorage.getMotualFriends(userId, otherId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        return userStorage.update(user);
    }

    public Collection<User> getItems() {
        return userStorage.getItems();
    }

    public User getItem(Long userId) {
        return userStorage.getItem(userId);
    }

}
