package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface UserStorage extends Storage<User> {

    Set<Long> setFriend(Long userId, Long friendId);
    Set<Long> removeFriend(Long userId, Long friendId);
    Set<Long> getUserFriends(Long userId);
    Set<Long> getMotualFriends(Long userId, Long otherId);

}
