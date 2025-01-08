package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.AfterHistDate;
import java.time.LocalDate;

@Data
public class Film {

    private Long id;

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

    /*
    @JsonProperty("duration")
    public Duration getDurationFromMinutes() {
        return Duration.ofMinutes(duration);
    } */

}
