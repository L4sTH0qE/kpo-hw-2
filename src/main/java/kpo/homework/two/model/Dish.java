package kpo.homework.two.model;

// Класс Dish (для запоминания ключевой информации о блюде из заказа даже после удаления соответствующего экземпляра Food).
public class Dish {
    // Время приготовления блюда.
    private int cookTimeMS;

    public int getCookTimeMS() {
        return cookTimeMS;
    }

    // Цена блюда.
    private int price;

    public int getPrice() {
        return price;
    }

    // Название блюда
    private final String name;

    // Конструктор.
    public Dish(String name, int cookTimeMS, int price) {
        this.cookTimeMS = cookTimeMS;
        this.price = price;
        this.name = name;
    }

    public String toString() {
        return name + "; ";
    }
}
