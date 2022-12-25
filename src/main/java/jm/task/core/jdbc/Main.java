package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import jm.task.core.jdbc.util.Util;

/**
 *  Создание таблицы User(ов)
 *  Добавление 4 User(ов) в таблицу с данными на свой выбор. После каждого добавления должен быть вывод в
 *  консоль ( User с именем – name добавлен в базу данных )
 *  Получение всех User из базы и вывод в консоль ( должен быть переопределен toString в классе User)
 *  Очистка таблицы User(ов)
 *  Удаление таблицы
 */
public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Миша", "Михайлов", (byte) 12);
        userService.saveUser("Коля", "Николаев", (byte) 13);
        userService.saveUser("Серёжа", "Сергеев", (byte) 14);
        userService.saveUser("Василий", "Васильев", (byte) 15);

        //userService.removeUserById(4);

        userService.getAllUsers();

        userService.cleanUsersTable();

        userService.dropUsersTable();

        Util.closeSessionFactory();
    }
}
