package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class UserDashboard extends BasePage<UserDashboard> {
    private SelenideElement userDashboard = $(Selectors.byText("User Dashboard"));
    private SelenideElement welcomText = $(Selectors.byClassName("welcome-text"));
    private SelenideElement depositMoney = $(Selectors.byText("\uD83D\uDCB0 Deposit Money"));
    private SelenideElement makeTransfer = $(Selectors.byText("\uD83D\uDD04 Make a Transfer"));
    private SelenideElement createNewAccount = $(Selectors.byText("➕ Create New Account"));

    @Override
    public String url() {
        return "/dashboard";
    }

    public UserDashboard depositMoney() {
        depositMoney.click();
        return this;
    }

    public UserDashboard makeTransfer() {
        makeTransfer.click();
        return this;
    }

    public UserDashboard createNewAccount() {
        createNewAccount.click();
        return this;
    }
}
