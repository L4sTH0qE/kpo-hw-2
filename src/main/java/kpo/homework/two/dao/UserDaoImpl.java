package kpo.homework.two.dao;

import kpo.homework.two.model.User;

import java.io.File;
import java.util.*;

// Класс UserDaoImpl, реализующий интерфейс Dao.
public class UserDaoImpl implements Dao<User> {

    // Коллекция пользователей.
    private static final Map<Integer, User> users = new HashMap<>();


    // Метод для получения всех объектов из коллекции.
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }


    // Метод для записи коллекции в файл.
    @Override
    public void saveData() {
        try {
            File file = new File("data/users.json");
            objectMapper.writeValue(file, new ArrayList<>(users.values()));
        }
        catch (Exception ex) {
            System.out.println("Error: cannot save user data to file!");
        }
    }

    // Метод для добавления объекта в коллекцию.
    @Override
    public void create(User user) {
        users.put(user.getId(), user);
        saveData();
    }

    // Метод для удаления объекта из коллекции.
    @Override
    public void delete(User user) {
        users.remove(user.getId());
        saveData();
    }

    // Метод для получения объекта из коллекции по логину и статусу.
    public User read(String login, boolean isAdmin) {
        for (User user : users.values()) {
            if (Objects.equals(user.getLogin(), login) && user.getIsAdmin() == isAdmin) {
                return user;
            }
        }
        return null;
    }
}
