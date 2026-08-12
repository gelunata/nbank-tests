package common.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Утилитарный класс для повторного выполнения операций при возникновении сбоев (Retry механизм).
 * Предоставляет методы для ожидания выполнения условий в автотестах.
 */
public class RetryUtils {
    /**
     * Повторяет выполнение действия до тех пор, пока оно не завершится успешно или не исчерпает попытки.
     *
     * @param action      выполняемое действие, не возвращающее результат
     * @param condition   условие успешности выполнения действия
     * @param maxAttempts максимальное количество попыток выполнения
     * @param delayMillis время ожидания в миллисекундах между попытками
     */
    public static <T> T retry(
            Supplier<T> action,
            Predicate<T> condition,
            int maxAttempts,
            long delayMillis
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
