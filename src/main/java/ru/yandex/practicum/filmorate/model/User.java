package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;
    private String name;
    private Set<Long> friends;

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
        this.friends = new HashSet<>();
    }
}
