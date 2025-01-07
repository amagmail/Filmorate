package ru.yandex.practicum.filmorate;

public class UserValidationTests {

    /*
    @Test
    public void CheckUserCreate() {

        System.out.println(">> Создать пользователя");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        User user = new User();
        user.setLogin("USER");
        user.setEmail("USER@mail.ru");
        user.setBirthday(LocalDate.parse("1950-01-01", formatter));
        System.out.println(user);

        UserController controller = new UserController();
        User u1 = controller.create(user);
        System.out.println(u1);
        User u2 = controller.create(user);
        System.out.println(u2);

        Assertions.assertEquals(u1.getLogin(), u1.getName(), "Ошибка валидации");
        Assertions.assertEquals(u2.getLogin(), u2.getName(), "Ошибка валидации");
        Assertions.assertEquals(controller.findAll().size(),  2);
    } */

}
