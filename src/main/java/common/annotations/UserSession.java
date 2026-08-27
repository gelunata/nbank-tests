package common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для конфигурации пользовательской сессии перед выполнением теста.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserSession {
    /**
     * Значение, используемое для настройки параметров сессии.
     *
     * @return количество создаваемых сессий или их идентификатор (по умолчанию 1)
     */
    int value() default 1;

    /**
     * Номер или индекс пользователя для авторизации в рамках теста.
     *
     * @return порядковый номер пользователя (по умолчанию 1)
     */
    int auth() default 1;
}
