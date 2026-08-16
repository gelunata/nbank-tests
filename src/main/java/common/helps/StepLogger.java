package common.helps;


import io.qameta.allure.Allure;

/*
 Example of usage:

 StepLogger.log("Get all users", () -> {

  click() ->  click log
  post() -> post log

  }

  "Get all users" ->
        "click log"
        "post log"

 */
public final class StepLogger {
    /**
     * Функциональный интерфейс для выполнения действий, возвращающих результат и способных выбросить исключение.
     *
     * @param <T> тип возвращаемого значения
     */
    @FunctionalInterface
    public interface ThrowableRunnable<T> {
        /**
         * Выполняет действие и возвращает результат.
         *
         * @return результат выполнения
         * @throws Throwable если в процессе выполнения произошла ошибка
         */
        T run() throws Throwable;
    }

    /**
     * Функциональный интерфейс для выполнения действий, не возвращающих результат и способных выбросить исключение.
     */
    @FunctionalInterface
    public interface ThrowableVoidRunnable {
        /**
         * Выполняет действие без возврата результата.
         *
         * @throws Throwable если в процессе выполнения произошла ошибка
         */
        void run() throws Throwable;
    }

    /**
     * Приватный конструктор для предотвращения создания экземпляров утилитарного класса.
     */
    private StepLogger() {
        throw new UnsupportedOperationException("Это утилитарный класс, его экземпляр не может быть создан");
    }

    /**
     * Логирует выполнение действия в Allure, возвращающего результат.
     *
     * @param <T>      тип возвращаемого результата
     * @param title    заголовок шага для Allure
     * @param runnable выполняемое действие с результатом
     * @return результат выполнения переданного действия
     */
    public static <T> T log(final String title, final ThrowableRunnable<T> runnable) {
        return Allure.step(title, () -> runnable.run());
    }

    /**
     * Логирует выполнение действия в Allure, не возвращающего результат.
     *
     * @param title    заголовок шага для Allure
     * @param runnable выполняемое действие без результата
     */
    public static void log(final String title, final ThrowableVoidRunnable runnable) {
        Allure.step(title, () -> {
            runnable.run();
            return null;
        });
    }
}
