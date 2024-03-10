package kpo.homework.two.model;

import kpo.homework.two.thread.OrderThread;
import java.util.*;

// Класс Order.
public class Order {
    // Счетчик для добавления новых заказов.
    private static int GLOBAL_ID = 0;

    public OrderThread getThread() {
        return thread;
    }

    // Поток для обработки заказа.
    public final OrderThread thread = new OrderThread(this);


    // Уникальный Id для каждого заказа.
    private int id;

    public int getId() {
        return id;
    }

    // Уникальный Id посетителя, сделавшего заказ.
    private final int userId;

    public int getUserId() {
        return userId;
    }

    // Список конкретных блюд в заказе.
    public List<Dish> order;


    // Оценка заказа.
    private int mark = -1;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    // Отзыв к заказу.
    private String comment = "none";

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Статус заказа.
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // Конструктор.
    public Order(int userId, List<Dish> order) {
        this.userId = userId;
        this.order = order;
        this.id = ++GLOBAL_ID;
        this.status = OrderStatus.Accepted;
    }

    // Добавление отзыва к заказу
    public void AddComment(int mark, String comment) {
        if (this.status == OrderStatus.Paid) {
            this.mark = mark;
            this.comment = comment;
        }
    }

    // Добавление блюда в заказ.
    public void AddDish(Dish dish) {
        order.add(dish);
    }

    public String toString() {
        String s = "";
        synchronized (this) {
            s = "Id='" + id + "'; Status='" + status + "'; dishes: ";
            for (Dish dish : order) {
                s += dish;
            }
        }
        return s;
    }

    // Метод для подсчета общей стоимости заказа.
    public int CountProfit() {
        int amount = 0;
        for (Dish dish : order) {
            amount += dish.getPrice();
        }
        return amount;
    }
}
