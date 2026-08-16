package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class DepositPage extends BasePage<DepositPage> {
    private final SelenideElement accountSelector = $(".form-control.account-selector");
    private final SelenideElement amountInput = $(".form-control.deposit-input");
    private final SelenideElement depositButton = $(Selectors.byText("\uD83D\uDCB5 Deposit"));

    @Override
    public String url() {
        return "/deposit";
    }

    public DepositPage deposit(double amount) {
        amountInput.sendKeys(String.valueOf(amount));
        depositButton.click();
        return this;
    }

    public DepositPage deposit(String account, double amount) {
        accountSelector.selectOptionContainingText(account);
        return deposit(amount);
    }
}
