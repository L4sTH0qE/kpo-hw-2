package kpo.homework.two.dao;

import kpo.homework.two.model.Food;

import java.util.*;
import java.io.File;

// Класс FoodDaoImpl, реализующий интерфейс Dao.
public class FoodDaoImpl implements Dao<Food> {

    // Меню.
    private static final Map<Integer, Food> menu = new HashMap<>();

    // Метод для получения всех блюд, доступных в меню.
    public List<Food> getAll() {
        return new ArrayList<Food>(menu.values());
    }

    // Метод для записи коллекции в файл.
    public void saveData() {
        try {
            File file = new File("data/menu.json");
            objectMapper.writeValue(file, new ArrayList<Food>(menu.values()));
        }
        catch (Exception ex) {
            System.out.println("Error: cannot save menu data to file!");
        }
    }

    // Метод для добавления объекта в коллекцию.
    public void create(Food food) {
        menu.put(food.getId(), food);
        saveData();
    }

    // Метод для удаления объекта из коллекции.
    public void delete(Food food) {
        menu.remove(food.getId());
        saveData();
    }

    // Метод для изменения объекта из коллекции.
    public void edit(int id, int cookTimeMS, int price, int quantity) {
        Food food = read(id);
        food.setPrice(price);
        food.setQuantity(quantity);
        food.setCookTimeMS(cookTimeMS);
        menu.replace(id, food);
        saveData();
    }

    // Метод для получения объекта из коллекции по id.
    public Food read(int id) {
        return menu.get(id);
    }
}
