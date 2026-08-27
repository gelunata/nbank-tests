package ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Страница авторизации пользователя.
 */
public class LoginPage extends BasePage<LoginPage> {
    private SelenideElement button = $("button");

    /**
     * Возвращает относительный URL страницы авторизации.
     *
     * @return строка пути URL
     */
    @Override
    public String url() {
        return "/login";
    }

    /**
     * Выполняет вход в систему с указанными учетными данными.
     *
     * @param username имя пользователя для входа
     * @param password пароль пользователя для входа
     * @return текущий экземпляр страницы LoginPage
     */
    public LoginPage login(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        button.click();
        return this;
    }
}
