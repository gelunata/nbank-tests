package ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

/**
 * Класс страницы пользователя.
 */
@Getter
public class UserPage extends BaseElement {
    /** Элемент отображения имени пользователя. */
    private final String username;
    /** Элемент отображения роли пользователя. */
    private final String role;

    /**
     * Конструктор страницы пользователя.
     *
     * @param element корневой элемент страницы или контекста
     */
    public UserPage(SelenideElement element) {
        super(element);
        username = element.getText().split("\n")[0];
        role = element.getText().split("\n")[1];
    }
}
