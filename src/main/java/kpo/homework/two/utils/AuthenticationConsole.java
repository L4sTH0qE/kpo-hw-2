package kpo.homework.two.utils;

import kpo.homework.two.model.User;

import java.io.IOException;
import java.util.Objects;

// Класс AuthenticationConsole для входа в приложение.
public class AuthenticationConsole {

    // Метод для отображения окна входа в программу.
    public static void StartAuthenticationLoop() throws Exception {
        try {
            Console.clear();
            // Выводим меню и ждем выбора пользователя, пока не будет выбрана опция "q" (завершение работы)
            while (true) {
                System.out.println("Authentication");
                System.out.println("1 - Enter as customer");
                System.out.println("2 - Enter as administrator");
                System.out.println("q - Exit");
                System.out.println();
                System.out.print("Your input> ");

                String choice = Console.scanner.nextLine();
                System.out.println();

                switch (choice) {
                    case "1":
                        EnterUser(false);
                        break;
                    case "2":
                        EnterUser(true);
                        break;
                    case "q":
                        Console.exit();
                        break;
                    default:
                        System.out.println("Invalid command.");
                }
            }
            // При ошибке во время работы программы.
        } catch (Exception ex) {
            System.out.println("Error: An unexpected error occurred while the program was running! Exiting...");
            System.exit(0);
        }
    }

    // Метод для авторизации пользователя.
    private static void EnterUser(boolean isAdmin) throws Exception {
        Console.clear();
        if (isAdmin) {
            boolean flag = true;
            while (flag) {
                try {
                    System.out.println("\nEnter admin code:");
                    System.out.print("Your input> ");
                    String code = Console.scanner.nextLine();

                    System.out.println();
                    if (Objects.equals(code, User.adminCode)) {
                        flag = false;
                    } else {
                        System.out.println("Incorrect admin code. Enter q to stop authentication (or something else to try again)");
                        System.out.print("Your input> ");
                        if (Objects.equals(Console.scanner.nextLine(), "q")) {
                            return;
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error: An unexpected error!");
                }
            }
        }
        String login = getLogin(isAdmin);
        String password = getPassword(login, isAdmin);
        User user = Console.userDao.read(login, isAdmin);
        if (user == null) {
            user = new User(login, password, isAdmin);
            Console.currentUser = user;
            Console.userDao.create(user);
            System.out.println("Entering new account...");
        } else {
            Console.currentUser = user;
            System.out.println("Entering account...");
        }

        if (isAdmin) {
            AdminConsole.StartAdminLoop();
        } else {
            CustomerConsole.StartCustomerLoop();
        }
    }

    // Метод для получения логина пользователя.
    private static String getLogin(boolean isAdmin) throws IOException {
        String login = "";
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("\nEnter login:");
                System.out.print("Your input> ");
                login = Console.scanner.nextLine();

                System.out.println();
                if (login.isEmpty()) {
                    System.out.println("Error: Login cannot be empty string!");
                } else {
                    flag = false;
                    User user = Console.userDao.read(login, isAdmin);
                    if (user != null) {
                        System.out.println("There is user with such login. Enter this account? (y/n)");
                        System.out.print("Your input> ");
                        String choice = Console.scanner.nextLine();
                        System.out.println();

                        switch (choice) {
                            case "y":
                                break;
                            case "n":
                                flag = true;
                                break;
                            default:
                                System.out.println("Invalid command. Default option is no");
                                flag = true;
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error: An unexpected error!");
            }
        }
        return login;
    }

    // Метод для получения пароля пользователя.
    private static String getPassword(String login, boolean isAdmin) throws IOException {
        String password = "";
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("\nEnter password:");
                System.out.print("Your input> ");
                password = Console.scanner.nextLine();

                System.out.println();
                if (password.isEmpty()) {
                    System.out.println("Error: Password cannot be empty string!");
                } else {
                    User user = Console.userDao.read(login, isAdmin);
                    if (user == null || Objects.equals(user.getPassword(), password)) {
                        flag = false;
                    } else {
                        System.out.println("Incorrect password. Try again!");
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error: An unexpected error!");
            }
        }
        return password;
    }
}
