package kpo.homework.two.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

// Общий интерфейс Dao<T>.
public interface Dao<T> {
    // Json-сериализатор для работы с файлами.
    static ObjectMapper objectMapper = new ObjectMapper();

    // Метод для получения всех объектов из коллекции.
    List<T> getAll();

    // Метод для добавления объекта в коллекцию.
    void create(T t);

    // Метод для удаления объекта из коллекции.
    void delete(T t);

    // Метод для записи коллекции в файл.
    void saveData();
}