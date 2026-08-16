package ui.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

/**
 * Базовый класс для всех элементов интерфейса.
 */
public abstract class BaseElement {

    // CHECKSTYLE:OFF: VisibilityModifier
    /** Ссылка на базовый Selenide элемент. */
    private final SelenideElement element;
    // CHECKSTYLE:ON: VisibilityModifier

    /**
     * Конструктор базового элемента.
     *
     * @param rootElement инициализирующий элемент локатора
     */
    public BaseElement(final SelenideElement rootElement) {
        this.element = rootElement;
    }

    /**
     * Поиск вложенного элемента по локатору By.
     *
     * @param selector локатор элемента
     * @return найденный вложенный элемент
     */
    protected final SelenideElement find(final By selector) {
        return element.find(selector);
    }

    /**
     * Поиск вложенного элемента по CSS селектору.
     *
     * @param cssSelector CSS селектор элемента
     * @return найденный вложенный элемент
     */
    protected final SelenideElement find(final String cssSelector) {
        return element.find(cssSelector);
    }

    /**
     * Поиск коллекции вложенных элементов по локатору By.
     *
     * @param selector локатор элементов
     * @return коллекция найденных элементов
     */
    protected final ElementsCollection findAll(final By selector) {
        return element.findAll(selector);
    }

    /**
     * Поиск коллекции вложенных элементов по CSS селектору.
     *
     * @param cssSelector CSS селектор элементов
     * @return коллекция найденных элементов
     */
    protected final ElementsCollection findAll(final String cssSelector) {
        return element.findAll(cssSelector);
    }
}
