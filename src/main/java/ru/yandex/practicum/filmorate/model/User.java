package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String name;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate birthday;

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }
}
