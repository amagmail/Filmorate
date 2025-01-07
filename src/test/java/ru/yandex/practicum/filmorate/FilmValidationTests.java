package ru.yandex.practicum.filmorate;

public class FilmValidationTests {

    /*
    @Test
    public void CheckFilmCreate() {

        System.out.println(">> Создать фильм");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Film film = new Film();
        film.setName("TEXT");
        film.setDescription("TEXT");
        film.setReleaseDate(LocalDate.parse("1995-12-28", formatter));
        film.setDuration(100);
        System.out.println(film);

        FilmController controller = new FilmController();
        Film f1 = controller.create(film);
        System.out.println(f1);
        Film f2 = controller.create(film);
        System.out.println(f2);

        LocalDate dateFrom = LocalDate.of(1895, 12, 28);
        Assertions.assertFalse(f1.getReleaseDate().isBefore(LocalDate.from(dateFrom)));
        Assertions.assertTrue(f1.getDuration() > 0);
        Assertions.assertNotNull(f1.getName());
        Assertions.assertEquals(controller.findAll().size(),  2);
    } */

}
