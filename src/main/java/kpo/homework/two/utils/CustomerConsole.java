package kpo.homework.two.utils;

import kpo.homework.two.model.*;
import kpo.homework.two.thread.DishThread;

import java.io.IOException;
import java.util.*;

// Класс CustomerConsole для работы посетителя.
public class CustomerConsole {
    // Метод для отображения главного меню посетителя.
    public static void startCustomerLoop() throws Exception {
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
                System.out.println("6 - Give feedback to the order");
                System.out.println("q - Exit");
                System.out.println();
                System.out.print("Your input> ");

                String choice = Console.scanner.nextLine();
                System.out.println();

                switch (choice) {
                    case "1":
                        showOrders();
                        break;
                    case "2":
                        addOrder();
                        break;
                    case "3":
                        removeOrder();
                        break;
                    case "4":
                        payOrder();
                        break;
                    case "5":
                        addDishToOrder();
                        break;
                    case "6":
                        giveFeedback();
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
        showOrders();
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
    private static Dish chooseDish() throws IOException {
        Console.showMenu();
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
    private static void addDishToOrder() throws IOException {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.ACCEPTED || OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.PREPARING)) {
                System.out.println("This order is not active. Returning to main menu...");
                return;
            }
            Dish dish = chooseDish();
            if (dish == null) {
                System.out.println("Dish is not added to order. Returning to main menu...");
            } else {
                Order order = OrderHandler.INSTANCE.read(orderId);
                DishThread dishThread = new DishThread(dish.getCookTimeMS());
                dishThread.start();
                order.getThread().getThreadList().add(dishThread);
                OrderHandler.INSTANCE.edit(orderId, dish);
                System.out.println("Dish is successfully added to order. Returning to main menu...");
            }
        }
    }

    // Метод для оплаты готовых заказов.
    private static void payOrder() throws IOException {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.READY)) {
                System.out.println("This order is not ready. Returning to main menu...");
                return;
            }
            OrderHandler.INSTANCE.changeStatus(orderId, OrderStatus.PAID);
            System.out.println("Order is successfully paid.");
            Console.countProfit();
        }
    }

    // Метод для отмены активных заказов.
    private static void removeOrder() {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.ACCEPTED || OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.PREPARING)) {
                System.out.println("This order is not active. Returning to main menu...");
                return;
            }
            OrderHandler.INSTANCE.read(orderId).getThread().interrupt();
            System.out.println("Order is successfully removed from active orders. Returning to main menu...");
        }
    }

    // Метод для создания новых заказов.
    private static void addOrder() throws IOException {
        Order order = new Order(Console.currentUser.getId(), new ArrayList<Dish>());
        // Добавление блюд в заказ.
        while (true) {
            System.out.println("Would you like to add dish to new order? (y/n)");
            System.out.print("Your input> ");
            String choice = Console.scanner.nextLine();
            System.out.println();

            if (Objects.equals(choice, "y")) {
                Dish dish = chooseDish();
                if (dish != null) {
                    order.addDish(dish);
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
    private static void showOrders() {
        List<Order> orders = OrderHandler.INSTANCE.getOrders(Console.currentUser.getId());
        System.out.println("Your orders:");
        for (int i = 0; i < orders.size(); ++i) {
            synchronized (orders.get(i)) {
                System.out.println(orders.get(i));
            }
        }
        System.out.println();
    }

    // Метод для добавления отзыва к оплаченным заказам.
    private static void giveFeedback() throws IOException {
        int orderId = getOrderId();
        if (orderId == -1) {
            return;
        }
        synchronized (OrderHandler.INSTANCE.read(orderId)) {
            if (!(OrderHandler.INSTANCE.read(orderId).getStatus() == OrderStatus.PAID)) {
                System.out.println("This order is not paid. Returning to main menu...");
                return;
            } else if (OrderHandler.INSTANCE.read(orderId).getMark() != -1) {
                System.out.println("This order already has feedback. Returning to main menu...");
                return;
            }
            int mark = getMark();
            String comment = getComment();
            OrderHandler.INSTANCE.read(orderId).addComment(mark, comment);
            System.out.println("Order is provided with feedback. Thank you!");
        }
    }

    // Метод для получения комментария о заказе.
    private static String getComment() throws IOException {
        System.out.println();
        System.out.println("Enter comment:");
        System.out.print("Your input> ");
        return Console.scanner.nextLine();
    }

    // Метод для получения оценки заказа.
    private static int getMark() throws IOException {
        int mark= -1;
        System.out.println();
        System.out.println("Enter mark for order (1 to 5):");
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Your input> ");
                String choice = Console.scanner.nextLine();
                mark = Integer.parseInt(choice);
                System.out.println();
                if (mark < 1 || mark > 5) {
                    System.out.println("Error: Mark must be integer value between 1 and 5!");
                } else {
                    flag = false;
                }
            } catch (NumberFormatException ex1) {
                System.out.println("Error: Mark must be integer value between 1 and 5!");
            } catch (Exception ex2) {
                System.out.println("Error: Unexpected error!");
            }
        }
        return mark;
    }
}
