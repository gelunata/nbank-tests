package ui.iteration2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.generators.RandomData;
import api.models.AccountResponse;
import api.models.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import ui.SoftAssertionsTest;

import java.util.Locale;

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferAgainTest extends SoftAssertionsTest {
    @Test
    public void userCanTransferMoneyAgainTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);
        AccountsSteps.transferMoney(userAuthHeader, sender.getId(), recipient.getId(), amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/transfer");

        $(byText("🔁 Transfer Again")).click();

        $("ul.list-group")
                .find(byText("TRANSFER_IN"))
                .parent()
                .find(withText("🔁 Repeat"))
                .click();


        $(Selectors.byText("-- Choose an account --")).parent().selectOptionContainingText(recipient.getAccountNumber());
        $("#confirmCheck").setSelected(true);
        $(byText("\uD83D\uDE80 Send Transfer")).click();

        Alert alert = switchTo().alert();
        String amountRound = String.format(Locale.UK, "%.2f", amount).replaceAll("\\.?0+$", "");
        softly.assertThat(alert.getText())  // БАГ в выводе сообщения!!! Сообщение пишет, что перевел из recipient.getId() в recipient.getId()
                .contains(String.format("✅ Transfer of $%s successful from Account %d to %d!", amountRound, recipient.getId(), sender.getId()));
        alert.accept();

        $(byText("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        // БАГ Сообщение об успешности, а на самом деле через API ничего не перевелось. Сумма осталась на счетах.
        softly.assertThat(CustomerSteps.getBalance(userAuthHeader, sender.getId())).isEqualTo(amount);
        softly.assertThat(CustomerSteps.getBalance(userAuthHeader, recipient.getId())).isEqualTo(0.0);
    }

    @Test
    public void userCannotTransferMoneyAgainTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);
        AccountsSteps.transferMoney(userAuthHeader, sender.getId(), recipient.getId(), amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/transfer");

        $(byText("🔁 Transfer Again")).click();

        $("ul.list-group")
                .find(byText("TRANSFER_IN"))
                .parent()
                .find(withText("🔁 Repeat"))
                .click();

        $(Selectors.byText("-- Choose an account --")).parent().selectOptionContainingText(recipient.getAccountNumber());
        $("input.form-control[type='number']").setValue(String.format(Locale.UK, "%.2f", amount + 0.01));
        $("#confirmCheck").setSelected(true);
        $(byText("\uD83D\uDE80 Send Transfer")).click();

        Alert alert = switchTo().alert();
        softly.assertThat("❌ Transfer failed: Please try again.")
                .contains(alert.getText());
        alert.accept();

        $(byText("\uD83D\uDD01 Repeat Transfer")).shouldBe(Condition.visible);
    }

    @Test
    public void buttonSendTransferDisabledIfAmountIsNotFilledTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);
        AccountsSteps.transferMoney(userAuthHeader, sender.getId(), recipient.getId(), amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/transfer");

        $(byText("🔁 Transfer Again")).click();

        $("ul.list-group")
                .find(byText("TRANSFER_IN"))
                .parent()
                .find(withText("🔁 Repeat"))
                .click();


        $(Selectors.byText("-- Choose an account --")).parent().selectOptionContainingText(recipient.getAccountNumber());
        $("input.form-control[type='number']").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE); // другие способы очистки не срабатывали
        $("#confirmCheck").setSelected(true);
        $(byText("\uD83D\uDE80 Send Transfer")).click();

        $("#confirmCheck").setSelected(false);
        $(byText("\uD83D\uDE80 Send Transfer")).shouldBe(disabled);
        // !!! БАГ, ну или предположение, что при незаполненном поле Amount кнопка не должна быть активна,
        // как и при других случаях, когда одно из других полей не заполнено.
    }

    @Test
    public void filterIsWorkingCorrectly() {
        double amount = RandomData.getDepositAmount();

        CreateUserRequest userRequest1 = AdminSteps.createUserRequest();
        String userAuthHeader1 = AdminSteps.createUser(userRequest1.getUsername(), userRequest1.getPassword());
        long id1 = AccountsSteps.createAccount(userAuthHeader1).getId();

        CreateUserRequest userRequest2 = AdminSteps.createUserRequest();
        String userAuthHeader2 = AdminSteps.createUser(userRequest2.getUsername(), userRequest2.getPassword());
        long id2 = AccountsSteps.createAccount(userAuthHeader2).getId();

        AccountsSteps.depositMoney(userAuthHeader1, id1, amount);
        AccountsSteps.transferMoney(userAuthHeader1, id1, id2, amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader1);
        Selenide.open("/transfer");

        $(byText("🔁 Transfer Again")).click();

        $(Selectors.byAttribute("placeholder", "Enter name to find transactions")).setValue(userRequest2.getUsername());
        $(byText("\uD83D\uDD0D Search Transactions")).click();

        long count = $("ul.list-group").findAll("li").stream().count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void filterIsWorkingCorrectlyIfNoMatches() {
        double amount = RandomData.getDepositAmount();

        CreateUserRequest userRequest1 = AdminSteps.createUserRequest();
        String userAuthHeader1 = AdminSteps.createUser(userRequest1.getUsername(), userRequest1.getPassword());
        long id1 = AccountsSteps.createAccount(userAuthHeader1).getId();

        CreateUserRequest userRequest2 = AdminSteps.createUserRequest();
        String userAuthHeader2 = AdminSteps.createUser(userRequest2.getUsername(), userRequest2.getPassword());
        AccountsSteps.createAccount(userAuthHeader2);

        AccountsSteps.depositMoney(userAuthHeader1, id1, amount);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader2);
        Selenide.open("/transfer");

        $(byText("🔁 Transfer Again")).click();

        $(Selectors.byAttribute("placeholder", "Enter name to find transactions")).setValue(userRequest1.getUsername());
        $(byText("\uD83D\uDD0D Search Transactions")).click();

        long count = $("ul.list-group").findAll("li").stream().count();
        assertThat(count).isEqualTo(0);
        // БАГ!!! Думаю при фильтрации должно отображаться те транзакции, в которых участвовал залогиненный пользователь.
        // Здесь пользователь видит транзакции другого пользователя, в которой текущий пользователь не участвует!
    }
}
