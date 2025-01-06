package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // Электронная почта не может быть пустой и должна содержать символ @
        if (!user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Поле email содержит невалидное значение");
        }
        // Логин не может быть пустым и содержать пробелы
        if (user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Поле login содержит невалидное значение");
        }
        // Имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        // Дата рождения не может быть в будущем
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Поле birthday содержит невалидное значение");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Идентификатор пользователя не может быть пустым");
            throw new ValidationException("Поле id содержит невалидное значение");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
                if (newUser.getEmail().contains("@")) {
                    oldUser.setEmail(newUser.getEmail());
                } else {
                    log.error("Электронная почта должна содержать символ @");
                    throw new ValidationException("Поле email содержит невалидное значение");
                }
            }
            if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) {
                if (newUser.getLogin().contains(" ")) {
                    log.error("Логин не может содержать пробелы");
                    throw new ValidationException("Поле login содержит невалидное значение");
                } else {
                    oldUser.setLogin(newUser.getLogin());
                }
            }
            if (newUser.getName() != null && !newUser.getName().isBlank()) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() != null && newUser.getBirthday().isBefore(LocalDate.now())) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            return oldUser;
        }
        log.error("Пользователь с идентификатором " + newUser.getId() + " не найден");
        throw new NotFoundException("Пользователь с идентификатором " + newUser.getId() + " не найден");
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
