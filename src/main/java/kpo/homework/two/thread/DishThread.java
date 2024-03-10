package kpo.homework.two.thread;

// Класс DishThread для приготовления одного блюда.
public class DishThread extends Thread {
    private final int cookingTime;

    public DishThread(int time) {
        this.cookingTime = time;
    }

    public void run() {
        try {
            Thread.sleep(cookingTime);
        }
        catch(InterruptedException e) {
            System.out.println("DishThread has been interrupted!");
        }
    }
}
