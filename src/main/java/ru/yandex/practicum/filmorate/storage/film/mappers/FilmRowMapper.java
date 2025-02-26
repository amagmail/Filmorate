package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setMpa(resultSet.getInt("mpa"));

        //film.setGenre(resultSet.getInt("genre"));
        //Array q2 = resultSet.getArray("genre");

        Set<Long> likes = new HashSet<>();
        long cnt = resultSet.getLong("likes");
        likes.add(cnt);
        film.setLikes(likes);

        /*
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("mpa"));
        film.setMpa(mpa);*/

        return film;
    }

}
