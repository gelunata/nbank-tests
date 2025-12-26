package ui.iteration2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.generators.RandomData;
import api.models.AccountResponse;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import ui.BaseTest;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositTest extends BaseTest {
    @Test
    public void userCanDepositMoneyIntoHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse account = AccountsSteps.createAccount(userAuthHeader);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).click();

        $(".form-control.account-selector").selectOption(1);
        $(".form-control.deposit-input").sendKeys(String.valueOf(amount));
        $(Selectors.byText("\uD83D\uDCB5 Deposit")).click();

        Alert alert = switchTo().alert();
        assertThat(alert.getText())
                .contains(String.format("✅ Successfully deposited $%s to account %s!", amount, account.getAccountNumber()));
        alert.accept();

        $(Selectors.byText("User Dashboard")).shouldBe(Condition.visible);

        assertEquals(amount, CustomerSteps.getBalance(userAuthHeader, account.getId()));
    }

    @Test
    public void userCannotDepositMoneyIntoHisAccountTest() {
        double amount = RandomData.getIncorrectDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        long id = AccountsSteps.createAccount(userAuthHeader).getId();

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).click();

        $(".form-control.account-selector").selectOption(1);
        $(".form-control.deposit-input").sendKeys(String.valueOf(amount));
        $(Selectors.byText("\uD83D\uDCB5 Deposit")).click();

        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains("❌ Please deposit less or equal to 5000$.");
        alert.accept();

        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).shouldBe(Condition.visible);

        assertEquals(0, CustomerSteps.getBalance(userAuthHeader, id));
    }

    @Test
    public void userCannotDepositMoneyIfNoAccountIsSelectedTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountsSteps.createAccount(userAuthHeader);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).click();

        $(".form-control.deposit-input").sendKeys(String.valueOf(amount));
        $(Selectors.byText("\uD83D\uDCB5 Deposit")).click();

        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains("❌ Please select an account.");
        alert.accept();

        $(Selectors.byText("\uD83D\uDCB0 Deposit Money")).shouldBe(Condition.visible);
    }
}
