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
import ui.SoftAssertionsTest;

import java.util.Locale;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest extends SoftAssertionsTest {
    @Test
    public void userCanTransferMoneyBetweenHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).click();

        $("select.account-selector").selectOptionContainingText(sender.getAccountNumber());
        $(Selectors.byAttribute("placeholder", "Enter recipient name")).sendKeys("");
        $(Selectors.byAttribute("placeholder", "Enter recipient account number"))
                .sendKeys(recipient.getAccountNumber());
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(amount));
        $("#confirmCheck").setSelected(true);
        $(Selectors.byText("\uD83D\uDE80 Send Transfer")).click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText())
                .contains(String.format(Locale.UK, "✅ Successfully transferred $%.2f to account %s!", amount, recipient.getAccountNumber()));
        alert.accept();

        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        softly.assertThat(0.0).isEqualTo(CustomerSteps.getBalance(userAuthHeader, sender.getId()));
        softly.assertThat(amount).isEqualTo(CustomerSteps.getBalance(userAuthHeader, recipient.getId()));
    }

    @Test
    public void userCannotTransferMoneyBetweenHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).click();

        $("select.account-selector").selectOptionContainingText(sender.getAccountNumber());
        $(Selectors.byAttribute("placeholder", "Enter recipient name")).sendKeys("");
        $(Selectors.byAttribute("placeholder", "Enter recipient account number"))
                .sendKeys(recipient.getAccountNumber());
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(amount + 0.01));
        $("#confirmCheck").setSelected(true);
        $(Selectors.byText("\uD83D\uDE80 Send Transfer")).click();

        Alert alert = switchTo().alert();
        softly.assertThat("❌ Error: Invalid transfer: insufficient funds or invalid accounts").contains(alert.getText());
        alert.accept();

        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        softly.assertThat(amount).isEqualTo(CustomerSteps.getBalance(userAuthHeader, sender.getId()));
        softly.assertThat(0.0).isEqualTo(CustomerSteps.getBalance(userAuthHeader, recipient.getId()));
    }

    @Test
    public void userCannotTransferMoneyIfAmountIsNotFilledTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).click();

        $("select.account-selector").selectOptionContainingText(sender.getAccountNumber());
        $(Selectors.byAttribute("placeholder", "Enter recipient name")).sendKeys("");
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(amount));
        $("#confirmCheck").setSelected(true);
        $(Selectors.byText("\uD83D\uDE80 Send Transfer")).click();

        Alert alert = switchTo().alert();
        assertThat("❌ Please fill all fields and confirm.").contains(alert.getText());
        alert.accept();

        $(Selectors.byText("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);
    }
}
