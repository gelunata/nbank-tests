package common.storage;

import api.models.CreateUserRequest;
import api.requests.steps.UserSteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Хранилище сессий пользователей для многопоточного выполнения тестов.
 * Использует {@link ThreadLocal} для изоляции данных между потоками.
 */
public class SessionStorage {
    /**
     * Потокобезопасный экземпляр хранилища для текущего потока.
     */
    private static final ThreadLocal<SessionStorage> INSTANCE = ThreadLocal.withInitial(SessionStorage::new);

    /**
     * Карта, связывающая запросы на создание пользователей с их шагами API.
     */
    private final LinkedHashMap<CreateUserRequest, UserSteps> userStepsMap = new LinkedHashMap<>();

    /**
     * Приватный конструктор для предотвращения создания экземпляров извне.
     */
    private SessionStorage() {
    }

    /**
     * Добавляет список пользователей в текущую сессию и инициализирует их шаги API.
     *
     * @param users список запросов на создание пользователей
     */
    public static void addUsers(List<CreateUserRequest> users) {
        for (CreateUserRequest user : users) {
            INSTANCE.get().userStepsMap.put(user, new UserSteps(user.getUsername(), user.getPassword()));
        }
    }

    /**
     * Возвращает карту всех пользователей и их шагов API в текущей сессии.
     *
     * @return карта пользователей и их шагов
     */
    public static LinkedHashMap<CreateUserRequest, UserSteps> getUsers() {
        return INSTANCE.get().userStepsMap;
    }

    /**
     * Возвращает запрос пользователя по его порядковому номеру (начиная с 1).
     *
     * @param number порядковый номер пользователя
     * @return запрос на создание пользователя
     */
    public static CreateUserRequest getUser(int number) {
        return new ArrayList<>(INSTANCE.get().userStepsMap.keySet()).get(number - 1);
    }

    /**
     * Возвращает запрос первого созданного пользователя в сессии.
     *
     * @return первый запрос на создание пользователя
     */
    public static CreateUserRequest getUser() {
        return getUser(1);
    }

    /**
     * Возвращает объект шагов API (UserSteps) по порядковому номеру пользователя (начиная с 1).
     *
     * @param number порядковый номер пользователя
     * @return объект с шагами API пользователя
     */
    public static UserSteps getSteps(int number) {
        return new ArrayList<>(INSTANCE.get().userStepsMap.values()).get(number - 1);
    }

    /**
     * Возвращает объект шагов API (UserSteps) для первого пользователя в сессии.
     *
     * @return объект с шагами API первого пользователя
     */
    public static UserSteps getSteps() {
        return getSteps(1);
    }

    /**
     * Очищает сохраненные данные пользователей в текущей сессии.
     */
    public static void clear() {
        INSTANCE.get().userStepsMap.clear();
    }
}
