package kpo.homework.two.model;

// Класс User.
public class User {
    // Счетчик для добавления новых пользователей.
    private static int GLOBAL_ID = 0;

    // Специальный код для авторизации администраторов.
    public static final String adminCode = "IAMADMIN";


    public int getId() {
        return this.id;
    }

    // Уникальный Id для каждого пользователя.
    private final int id;

    public String getLogin() {
        return this.login;
    }

    // Логин пользователя.
    private final String login;

    public String getPassword() {
        return password;
    }

    // Пароль пользователя.
    private final String password;

    public boolean getIsAdmin() {
        return isAdmin;
    }

    // Является ли пользователь админом.
    private final boolean isAdmin;

    // Конструктор.
    public User(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.id = ++GLOBAL_ID;
    }

    public String toString() {
        return "Id='" + id + "'; Login='" + login + "'; Password='" + password + "'; IsAdmin='" + isAdmin + "'";
    }
}
