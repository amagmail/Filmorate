package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

import ru.yandex.practicum.filmorate.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}/friends/{friendId}")
    public Set<Long> setFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        /*
        User user1 = userService.getItem(userId);
        User user2 = userService.getItem(friendId);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        return user1.getFriends();*/
        return userService.setFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Set<Long> removeFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        /*
        User user1 = userService.getItem(userId);
        User user2 = userService.getItem(friendId);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        return user1.getFriends();*/
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Set<Long> getUserFriends(@PathVariable("userId") Long userId) {
        /*
        User user = userService.getItem(userId);
        return user.getFriends();*/
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Set<Long> getMotualFriends(@PathVariable("userId") Long userId, @PathVariable("otherId") Long otherId) {
        /*
        User user1 = userService.getItem(userId);
        User user2 = userService.getItem(otherId);
        Set<Long> mutualFriends = new HashSet<>(user1.getFriends());
        mutualFriends.retainAll(user2.getFriends());
        return mutualFriends;*/
        return userService.getMotualFriends(userId, otherId);
    }

    @GetMapping("/{userId}")
    public User getItem(@PathVariable("userId") Long userId) {
        return userService.getItem(userId);
    }

    @GetMapping
    public Collection<User> getItems() {
        return userService.getItems();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        // Электронная почта не может быть пустой и должна содержать спец символы: @NotNull, @NotBlank, @Email
        // Логин не может быть пустым и содержать пробелы: @NotNull, @NotBlank, @Pattern
        // Дата рождения не может быть в будущем: @Past

        // Имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Идентификатор пользователя не может быть пустым");
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        return userService.update(newUser);
    }
}
