package kpo.homework.two.thread;

import kpo.homework.two.model.Dish;
import kpo.homework.two.model.Order;
import kpo.homework.two.model.OrderStatus;

import java.util.*;

// Класс OrderThread для обработки одного заказа.
public class OrderThread extends Thread {
    private final Order order;
    private final List<DishThread> threadList = new ArrayList<>();
    public OrderThread(Order order) {
        this.order = order;
    }

    public List<DishThread> GetThreadList() { return threadList; }

    // Запуск потока на обработку заказа.
    public void run() {
        order.setStatus(OrderStatus.Preparing);
        // Все блюда из заказа готовятся в отдельных потоках.
        for (Dish dish : order.order) {
            DishThread dishThread = new DishThread(dish.getCookTimeMS());
            dishThread.start();
            threadList.add(dishThread);
        }

        // Ожидаем приготовления всех блюд.
        while (true) {
            for (int i = 0; i < threadList.size(); ++i) {
                try {
                    threadList.get(i).join();
                } catch (InterruptedException ex) {
                    // Поток прерван до завершения приготовления всех блюд.
                    synchronized (order) {
                        order.setStatus(OrderStatus.Rejected);
                    }
                    return;
                }
            }
            // Проверяем, что не осталось незавершенных потоков dishThread.
            synchronized (order) {
                for (int i = 0; i < threadList.size(); ++i) {
                    if (threadList.get(i).isAlive()) {
                        continue;
                    }
                }
                order.setStatus(OrderStatus.Ready);
                break;
            }
        }
    }
}
