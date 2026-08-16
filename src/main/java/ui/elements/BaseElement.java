package ui.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

/**
 * Базовый класс для всех элементов интерфейса.
 */
public abstract class BaseElement {
    protected final SelenideElement element;

    /**
     * Конструктор базового элемента.
     *
     * @param element инициализирующий элемент локатора
     */
    public BaseElement(SelenideElement element) {
        this.element = element;
    }

    /**
     * Поиск вложенного элемента по локатору By.
     *
     * @param selector локатор элемента
     * @return найденный вложенный элемент
     */
    protected SelenideElement find(By selector) {
        return element.find(selector);
    }

    /**
     * Поиск вложенного элемента по CSS селектору.
     *
     * @param cssSelector CSS селектор элемента
     * @return найденный вложенный элемент
     */
    protected SelenideElement find(String cssSelector) {
        return element.find(cssSelector);
    }

    protected ElementsCollection findAll(By selector) {
        return element.findAll(selector);
    }

    protected ElementsCollection findAll(String cssSelector) {
        return element.findAll(cssSelector);
    }
}
