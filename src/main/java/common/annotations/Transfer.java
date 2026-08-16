package common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transfer {
    /**
     * Порядковый номер пользователя для отправления депозита.
     *
     * @return номер пользователя
     */
    int authSender() default 1;

    /**
     * Порядковый номер пользователя для начисления депозита.
     *
     * @return номер пользователя
     */
    int authRecipient() default 1;
}
