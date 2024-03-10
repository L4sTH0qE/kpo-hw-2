package kpo.homework.two.model;

// Класс Food.
public class Food {
    // Счетчик для добавления новых блюд в меню.
    private static int GLOBAL_ID = 0;

    // Уникальный Id для каждого блюда.
    private int id;

    public int getId() {
        return id;
    }

    // Название блюда.
    private String name;

    public String getName() {
        return name;
    }

    // Время приготовления одной единицы данного блюда.
    private int cookTimeMS;

    public int getCookTimeMS() {
        return cookTimeMS;
    }

    public void setCookTimeMS(int cookTimeMS) {
        this.cookTimeMS = cookTimeMS;
    }

    // Количество доступных единиц данного блюда для приготовления.
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Цена одной единицы данного блюда.
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Конструктор.
    public Food(String name, int cookTimeMS, int quantity, int price) {
        this.name = name;
        this.cookTimeMS = cookTimeMS;
        this.price = price;
        this.quantity = quantity;
        this.id = ++GLOBAL_ID;
    }

    public String toString() {
        return "Id='" + id + "'; Name='" + name + "'; CookTimeMS='" + cookTimeMS + "'; Quantity='" + quantity + "'; Price='" + price + "'";
    }
}
