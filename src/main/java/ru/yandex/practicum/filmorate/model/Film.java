package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.AfterHistDate;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;
    private Set<Long> likes;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @AfterHistDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    public Film(String name) {
        this.name = name;
        this.likes = new HashSet<>();
    }

    public Film() {
        this.likes = new HashSet<>();
    }

    public static int compareLikes(Film f1, Film f2) {
        return f2.likes.size() - f1.likes.size();
    }
}
