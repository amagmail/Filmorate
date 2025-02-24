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
    private Set<Long> likes = new HashSet<>();

    private Integer genre;
    private Integer mpa;

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
    }

    public Film() {

    }

    public static int compareLikes(Film f1, Film f2) {
        return f2.likes.size() - f1.likes.size();
    }

}
