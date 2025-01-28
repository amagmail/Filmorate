package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

import ru.yandex.practicum.filmorate.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}/friends/{friendId}")
    public Set<Long> setFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        return userService.setFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Set<Long> removeFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Set<Long> getUserFriends(@PathVariable("userId") Long userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Set<Long> getMotualFriends(@PathVariable("userId") Long userId, @PathVariable("otherId") Long otherId) {
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
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        // Электронная почта не может быть пустой и должна содержать спец символы: @NotNull, @NotBlank, @Email
        // Логин не может быть пустым и содержать пробелы: @NotNull, @NotBlank, @Pattern
        // Дата рождения не может быть в будущем: @Past
        // Имя для отображения может быть пустым — в таком случае будет использован логин
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        return new ErrorResponse(
                "Ошибка валидации",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return new ErrorResponse(
                "Искомый объект не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final RuntimeException e) {
        return new ErrorResponse(
                "Возникло исключение",
                e.getMessage()
        );
    }
}
