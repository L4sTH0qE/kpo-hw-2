package kpo.homework.two.utils;

import kpo.homework.two.model.*;
import kpo.homework.two.thread.DishThread;

import java.io.IOException;
import java.util.*;

// Класс CustomerConsole для работы посетителя.
public class CustomerConsole {
    // Метод для отображения главного меню посетителя.
    public static void StartCustomerLoop() throws Exception {
        try {
            Console.clear();
            // Выводим меню и ждем выбора пользователя, пока не будет выбрана опция "q" (завершение работы)
            while (true) {
                System.out.println("Customer's options:");
                System.out.println("1 - Show orders");
                System.out.println("2 - Make a new order");
                System.out.println("3 - Cancel order");
                System.out.println("4 - Pay for the order");
                System.out.println("5 - Add dish to the order");
                System.out.println("q - Exit");
                System.out.println();
                System.out.print("Your input> ");

                String choice = Console.scanner.nextLine();
                System.out.println();

                switch (choice) {
                    case "1":
                        ShowOrders();
                        break;
                    case "2":
                        AddOrder();
                        break;
                    case "3":
                        RemoveOrder();
                        break;
                    case "4":
                        PayOrder();
                        break;
                    case "5":
                        AddDishToOrder();
                        break;
                    case "q":
                        return;
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

    // Метод для получения id заказа пользователя.
    private static int getOrderId() {
        if (OrderHandler.INSTANCE.getOrders(Console.currentUser.getId()).isEmpty()) {
            System.out.println("No orders! Return to main menu...");
            return -1;
        }
        ShowOrders();
        System.out.println();
        System.out.println("Enter available order id:");
        while (true) {
            try {
                System.out.print("Your input> ");
                String choice = Console.scanner.nextLine();
                int id = Integer.parseInt(choice);
                System.out.println();
                if (OrderHandler.INSTANCE.read(id) == null || OrderHandler.INSTANCE.read(id).getUserId() != Console.currentUser.getId()) {
                    System.out.println("Error: No order with this id!");
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

    // Метод для выбора блюда.
    private static Dish ChooseDish() throws IOException {
        Console.ShowMenu();
        if (Console.foodDao.getAll().isEmpty()) {
            System.out.println("No dishes available.");
            return null;
        }
        int id = Console.getFoodId();
        if (Console.foodDao.read(id).getQuantity() <= 0) {
            System.out.println("This dish is out of stock.");
            return null;
        }
        String name = Console.foodDao.read(id).getName();
        int cookTimeMS = Console.foodDao.read(id).getCookTimeMS();
        int price = Console.foodDao.read(id).getPrice();
        int quantity = Console.foodDao.read(id).getQuantity() - 1;
        Console.foodDao.edit(id, cookTimeMS, price, quantity);
        return new Dish(name, cookTimeMS, price);
    }

    // Метод для добавления блюда в активный заказ.
    private static void AddDishToOrder() throws IOException {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.Accepted || OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.Preparing)) {
                System.out.println("This order is not active. Returning to main menu...");
                return;
            }
            Dish dish = ChooseDish();
            if (dish == null) {
                System.out.println("Dish is not added to order. Returning to main menu...");
            } else {
                Order order = OrderHandler.INSTANCE.read(orderId);
                DishThread dishThread = new DishThread(dish.getCookTimeMS());
                dishThread.start();
                order.getThread().GetThreadList().add(dishThread);
                OrderHandler.INSTANCE.edit(orderId, dish);
                System.out.println("Dish is successfully added to order. Returning to main menu...");
            }
        }
    }

    // Метод для оплаты готовых заказов.
    private static void PayOrder() throws IOException {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.Ready)) {
                System.out.println("This order is not ready. Returning to main menu...");
                return;
            }
            OrderHandler.INSTANCE.ChangeStatus(orderId, OrderStatus.Paid);
            System.out.println("Order is successfully paid.");
            Console.CountProfit();
        }
    }

    // Метод для отмены активных заказов.
    private static void RemoveOrder() {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.Accepted || OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.Preparing)) {
                System.out.println("This order is not active. Returning to main menu...");
                return;
            }
            OrderHandler.INSTANCE.read(orderId).getThread().interrupt();
            System.out.println("Order is successfully removed from active orders. Returning to main menu...");
        }
    }

    // Метод для создания новых заказов.
    private static void AddOrder() throws IOException {
        Order order = new Order(Console.currentUser.getId(), new ArrayList<Dish>());
        // Добавление блюд в заказ.
        while (true) {
            System.out.println("Would you like to add dish to new order? (y/n)");
            System.out.print("Your input> ");
            String choice = Console.scanner.nextLine();
            System.out.println();

            if (Objects.equals(choice, "y")) {
                Dish dish = ChooseDish();
                if (dish != null) {
                    order.AddDish(dish);
                }
            } else if (Objects.equals(choice, "n")) {
                break;
            } else {
                System.out.println("Invalid command. Default option is n");
                break;
            }
        }
        // Создание заказа и запуск соответствующего потока.
        order.getThread().start();
        OrderHandler.INSTANCE.create(order);
        System.out.println("New order created. Returning to main menu...");
    }

    // Метод для отображения всех заказов данного посетителя.
    private static void ShowOrders() {
        List<Order> orders = OrderHandler.INSTANCE.getOrders(Console.currentUser.getId());
        System.out.println("Your orders:");
        for (int i = 0; i < orders.size(); ++i) {
            synchronized (orders.get(i)) {
                System.out.println(orders.get(i));
            }
        }
        System.out.println();
    }
}
