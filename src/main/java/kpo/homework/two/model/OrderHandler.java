package kpo.homework.two.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Синглетон OrderHandler для обработки всех заказов.
public enum OrderHandler {
    INSTANCE;

    // Коллекция заказов.
    private static final Map<Integer, Order> orders = new HashMap<>();

    // Метод для получения всех объектов из коллекции.
    public List<Order> getAll() {
        return new ArrayList<>(orders.values());
    }

    // Метод для получения заказов данного посетителя (по id) из коллекции.
    public List<Order> getOrders(int userId) {
        ArrayList<Order> myOrders = new ArrayList<Order>();
        for (Order order : orders.values()) {
            if (order.getUserId() == userId) {
                myOrders.add(order);
            }
        }
        return myOrders;
    }

    // Метод для добавления объекта в коллекцию.
    public void create(Order order) {
        orders.put(order.getId(), order);
    }

    // Метод для добавления блюда в заказ из коллекции.
    public void edit(int id, Dish dish) {
        Order order = read(id);
        order.AddDish(dish);
        orders.replace(id, order);
    }

    // Метод для изменения статуса заказа из коллекции.
    public void ChangeStatus(int id, OrderStatus status) {
        Order order = read(id);
        order.setStatus(status);
        orders.replace(id, order);
    }

    // Метод для получения объекта из коллекции по id.
    public Order read(int id) {
        return orders.get(id);
    }
}
