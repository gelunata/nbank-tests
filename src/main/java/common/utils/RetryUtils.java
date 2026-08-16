package common.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Утилитарный класс для повторного выполнения операций при возникновении сбоев (Retry механизм).
 * Предоставляет методы для ожидания выполнения условий в автотестах.
 */
public final class RetryUtils {
    /**
     * Приватный конструктор для предотвращения создания экземпляров утилитарного класса.
     */
    private RetryUtils() {
        throw new UnsupportedOperationException("Это утилитарный класс, его экземпляр не может быть создан");
    }

    /**
     * Повторяет выполнение действия до тех пор, пока оно не завершится успешно или не исчерпает попытки.
     *
     * @param <T>         тип возвращаемого объекта и проверяемого значения в предикате
     * @param action      выполняемое действие, возвращающее результат
     * @param condition   условие успешности выполнения действия
     * @param maxAttempts максимальное количество попыток выполнения
     * @param delayMillis время ожидания в миллисекундах между попытками
     * @return результат успешного выполнения действия
     */
    public static <T> T retry(
            final Supplier<T> action,
            final Predicate<T> condition,
            final int maxAttempts,
            final long delayMillis
    ) {
        T result = null;
        int attempts = 0;
        while (attempts < maxAttempts) {
            attempts++;
            result = action.get();

            if (condition.test(result)) {
                return result;
            }

            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Retry failed after " + maxAttempts + "attempts!");
    }
}
