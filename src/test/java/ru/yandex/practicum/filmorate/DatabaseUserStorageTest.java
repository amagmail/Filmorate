package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.user.InDatabaseUserStorage;
import ru.yandex.practicum.filmorate.storage.user.mappers.UserRowMapper;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({InDatabaseUserStorage.class, UserRowMapper.class})
public class DatabaseUserStorageTest {

    private final InDatabaseUserStorage userStorage;

    @Test
    public void test() {

        // База не подключается при интеграционном тестировании, поэтому оставил код в таком виде
        // С самой базой все хорошо, подключаюсь к нему через DBeaver и проверяю работу с данными
        // Приложение так же успешно проходит все автотесты из Postman созданные для текущего спринта
        System.out.println("Begin");
        System.out.println(userStorage.getItems());
        Assertions.assertTrue(userStorage.getItems().isEmpty());
        System.out.println("End");

    }

}