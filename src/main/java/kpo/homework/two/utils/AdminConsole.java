package kpo.homework.two.utils;

import kpo.homework.two.model.Food;

import java.io.IOException;

// Класс AdminConsole для работы администратора.
public class AdminConsole {
    // Метод для отображения главного меню администратора.
    public static void StartAdminLoop() throws Exception {
        try {
            Console.clear();
            // Выводим меню и ждем выбора пользователя, пока не будет выбрана опция "q" (завершение работы)
            while (true) {
                System.out.println("Admin's options:");
                System.out.println("1 - Show menu");
                System.out.println("2 - Add dish");
                System.out.println("3 - Remove dish");
                System.out.println("4 - Edit dish");
                System.out.println("q - Exit");
                System.out.println();
                System.out.print("Your input> ");

                String choice = Console.scanner.nextLine();
                System.out.println();

                switch (choice) {
                    case "1":
                        Console.ShowMenu();
                        break;
                    case "2":
                        AddDish();
                        break;
                    case "3":
                        RemoveDish();
                        break;
                    case "4":
                        EditDish();
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

    // Метод для изменения блюда из меню.
    private static void EditDish() throws IOException {
        if (Console.foodDao.getAll().isEmpty()) {
            System.out.println("No dishes to edit in Menu. Returning to main menu...");
        } else {
            Console.ShowMenu();
            int id = Console.getFoodId(); // Id блюда.
            int cookTimeMS = getFoodCookTimeMS(); // Время готовки.
            int price = getFoodPrice(); // Цена блюда.
            int quantity = getFoodQuantity(); // Количество единиц блюда.

            Console.foodDao.edit(id, cookTimeMS, price, quantity);
            System.out.println("Editing dish is successful.");
            System.out.println("Returning to main menu...");
        }
    }

    // Метод для удаления блюда из меню.
    private static void RemoveDish() throws IOException {
        if (Console.foodDao.getAll().isEmpty()) {
            System.out.println("No dishes to remove in Menu. Returning to main menu...");
        } else {
            Console.ShowMenu();
            int id = Console.getFoodId(); // Id блюда.
            Console.foodDao.delete(Console.foodDao.read(id));
            System.out.println("Removing dish is successful.");
            System.out.println("Returning to main menu...");
        }
    }

    // Метод для добавления блюда в меню.
    private static void AddDish() throws IOException {
        Console.ShowMenu();

        String name = getFoodName(); // Название блюда.
        int cookTimeMS = getFoodCookTimeMS(); // Время готовки.
        int price = getFoodPrice(); // Цена блюда.
        int quantity = getFoodQuantity(); // Количество единиц блюда.

        // Добавление блюда.
        Food food = new Food(name, cookTimeMS, quantity, price);
        Console.foodDao.create(food);
        System.out.println("New dish added.");
        System.out.println("Returning to main menu...");
    }

    // Метод для получения названия нового блюда.
    private static String getFoodName() throws IOException {
        System.out.println();
        System.out.println("Enter dish name:");
        System.out.print("Your input> ");
        return Console.scanner.nextLine();
    }

    // Метод для получения цены нового блюда.
    private static int getFoodPrice() throws IOException {
        int price = -1;
        System.out.println();
        System.out.println("Enter dish price:");
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Your input> ");
                String choice = Console.scanner.nextLine();
                price = Integer.parseInt(choice);
                System.out.println();
                if (price<= 0) {
                    System.out.println("Error: Price must be positive integer value!");
                } else {
                    flag = false;
                }
            } catch (NumberFormatException ex1) {
                System.out.println("Error: Price must be positive integer value!");
            } catch (Exception ex2) {
                System.out.println("Error: Unexpected error!");
            }
        }
        return price;
    }

    // Метод для получения количества единиц нового блюда.
    private static int getFoodQuantity() throws IOException {
        int quantity = -1;
        System.out.println();
        System.out.println("Enter dish quantity:");
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Your input> ");
                String choice = Console.scanner.nextLine();
                quantity = Integer.parseInt(choice);
                System.out.println();
                if (quantity <= 0) {
                    System.out.println("Error: Quantity must be positive integer value!");
                } else {
                    flag = false;
                }
            } catch (NumberFormatException ex1) {
                System.out.println("Error: Quantity must be positive integer value!");
            } catch (Exception ex2) {
                System.out.println("Error: Unexpected error!");
            }
        }
        return quantity;
    }

    // Метод для получения времени готовки нового блюда.
    private static int getFoodCookTimeMS() throws IOException {
        int cookTime = -1;
        System.out.println();
        System.out.println("Enter dish cooking time (milliseconds):");
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("Your input> ");
                String choice = Console.scanner.nextLine();
                cookTime = Integer.parseInt(choice);
                System.out.println();
                if (cookTime <= 0) {
                    System.out.println("Error: Cooking time must be positive integer value!");
                } else {
                    flag = false;
                }
            } catch (NumberFormatException ex1) {
                System.out.println("Error: Cooking time must be positive integer value!");
            } catch (Exception ex2) {
                System.out.println("Error: Unexpected error!");
            }
        }
        return cookTime;
    }
}
