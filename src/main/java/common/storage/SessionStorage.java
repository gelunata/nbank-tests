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

    public static void addUsers(List<CreateUserRequest> users) {
        for (CreateUserRequest user : users) {
            INSTANCE.get().userStepsMap.put(user, new UserSteps(user.getUsername(), user.getPassword()));
        }
    }

    public static LinkedHashMap<CreateUserRequest, UserSteps> getUsers() {
        return INSTANCE.get().userStepsMap;
    }

    public static CreateUserRequest getUser(int number) {
        return new ArrayList<>(INSTANCE.get().userStepsMap.keySet()).get(number - 1);
    }

    public static CreateUserRequest getUser() {
        return getUser(1);
    }

    public static UserSteps getSteps(int number) {
        return new ArrayList<>(INSTANCE.get().userStepsMap.values()).get(number - 1);
    }

    public static UserSteps getSteps() {
        return getSteps(1);
    }

    public static void clear() {
        INSTANCE.get().userStepsMap.clear();
    }
}
