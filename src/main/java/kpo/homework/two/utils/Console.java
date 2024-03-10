package kpo.homework.two.utils;

import kpo.homework.two.model.*;
import kpo.homework.two.dao.*;

import java.io.*;
import java.util.*;

// Основной класс Console для работы приложения.
public class Console {

    // Метод для завершения работы программы.
    public static void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    // Метод для очистки окна консоли.
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Dao-объекты для используемых в программе сущностей.
    public static FoodDaoImpl foodDao = new FoodDaoImpl();
    public static UserDaoImpl userDao = new UserDaoImpl();

    // Пользователь, который в данный момент использует приложение.
    public static User currentUser;

    // Для получения ввода пользователя используется объект типа Scanner из состава JDK(JRE) API
    // Для инициализации scanner'а используется объект стандартного потока ввода - System.in
    public static Scanner scanner = new Scanner(System.in);


    // Метод для отображения меню.
    public static void ShowMenu() {
        List<Food> menu = foodDao.getAll();
        System.out.println("Menu:");
        for (Food food : menu) {
            System.out.println(food);
        }
        System.out.println();
    }

    // Метод для получения id блюда.
    public static int getFoodId() throws IOException {
        System.out.println();
        System.out.println("Enter dish id:");
        while (true) {
            try {
                System.out.print("Your input> ");
                String choice = Console.scanner.nextLine();
                int id = Integer.parseInt(choice);
                System.out.println();
                if (Console.foodDao.read(id) == null) {
                    System.out.println("Error: No dish with this id!");
                } else {
                    return id;
                }
            } catch (NumberFormatException ex1) {
                System.out.println("Error: Id must be integer value!");
            } catch (Exception ex2) {
                System.out.println("Error: Unexpected error!");
            }
        }
    }


    // Метод для записи общего дохода в файл.
    public static void CountProfit() {
        int profit = 0;
        List<Order> orders = OrderHandler.INSTANCE.getAll();
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.Paid) {
                profit += order.CountProfit();
            }
        }
        try {
            PrintWriter writer = new PrintWriter("data/profit.txt");
            writer.print(profit);
            writer.close();
        } catch (IOException ex) {
        }
    }
}
