package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Страница совершения депозита.
 */
public final class DepositPage extends BasePage<DepositPage> {

    /** Выпадающий список для выбора счета. */
    private final SelenideElement accountSelector = $(".form-control.account-selector");

    /** Поле ввода суммы депозита. */
    private final SelenideElement amountInput = $(".form-control.deposit-input");

    /** Кнопка подтверждения внесения депозита. */
    private final SelenideElement depositButton = $(Selectors.byText("\uD83D\uDCB5 Deposit"));

    /**
     * Возвращает относительный URL страницы депозита.
     *
     * @return строка пути URL
     */
    @Override
    public String url() {
        return "/deposit";
    }

    /**
     * Вносит указанную сумму на выбранный по умолчанию счет.
     *
     * @param amount сумма депозита
     * @return текущий экземпляр страницы DepositPage
     */
    public DepositPage deposit(final double amount) {
        amountInput.sendKeys(String.valueOf(amount));
        depositButton.click();
        return this;
    }

    /**
     * Выбирает указанный счет и вносит на него сумму.
     *
     * @param account номер или имя счета
     * @param amount сумма депозита
     * @return текущий экземпляр страницы DepositPage
     */
    public DepositPage deposit(final String account, final double amount) {
        accountSelector.selectOptionContainingText(account);
        return deposit(amount);
    }
}
