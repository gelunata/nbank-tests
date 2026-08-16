package common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Аннотация для автоматической настройки аккаунтов перед тестом.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Accounts {
    /**
     * Количество создаваемых аккаунтов для пользователя.
     *
     * @return количество аккаунтов
     */
    int value() default 1;
}
