package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import ru.yandex.practicum.filmorate.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public User getItem(@PathVariable("userId") Long userId){
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
