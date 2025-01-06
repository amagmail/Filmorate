package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {

    private Long id;
    private Integer duration;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    /*
    @JsonProperty("duration")
    public Duration getDurationFromMinutes() {
        return Duration.ofMinutes(duration);
    } */

}
