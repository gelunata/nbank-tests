package ui.iteration2;

import api.generators.RandomData;
import api.models.AccountResponse;
import api.models.CreateUserRequest;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.TransferPage;

import java.util.Locale;

import static com.codeborne.selenide.Condition.disabled;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferAgainTest extends BaseUiTest {
    @Test
    public void userCanTransferMoneyAgainTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);
        AccountsSteps.transferMoney(userAuthHeader, sender.getId(), recipient.getId(), amount);

        authAsUser(userAuthHeader);

        new TransferPage().open()
                .transferAgain()
                .repeat()
                .repeatTransfer(recipient.getAccountNumber(), amount, true)
                .checkAlertMessageAndAccept(BankAlert.TRANSFER_SUCCESSFUL_FROM_ACCOUNT_TO_ACCOUNT.format(
                        String.format(Locale.UK, "%.2f", amount).replaceAll("\\.?0+$", ""),
                        recipient.getId(), sender.getId()));
        // БАГ в выводе сообщения!!! Сообщение пишет, что перевел из recipient.getId() в recipient.getId()

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

        authAsUser(userAuthHeader);

        new TransferPage().open()
                .transferAgain()
                .repeat()
                .repeatTransfer(recipient.getAccountNumber(), amount + 0.01, true)
                .checkAlertMessageAndAccept(BankAlert.TRANSFER_FAILED_PLEASE_TRY_AGAIN.getMessage())
                .getRepeatTransferTitle()
                .shouldBe(Condition.visible);
    }

    @Test
    public void buttonSendTransferDisabledIfAmountIsNotFilledTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);
        AccountsSteps.transferMoney(userAuthHeader, sender.getId(), recipient.getId(), amount);

        authAsUser(userAuthHeader);
        new TransferPage().open()
                .transferAgain()
                .repeat()
                .fillValuesForRepeatTransfer(recipient.getAccountNumber(), true)
                .getTransferButton()
                .shouldBe(disabled);
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

        authAsUser(userAuthHeader1);

        long count = new TransferPage().open()
                .transferAgain()
                .searchTransactionByUsernameOrName(userRequest2.getUsername())
                .getMatchingTransactions()
                .stream().count();

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

        authAsUser(userAuthHeader2);

        long count = new TransferPage().open()
                .transferAgain()
                .searchTransactionByUsernameOrName(userRequest1.getUsername())
                .getMatchingTransactions()
                .stream().count();

        assertThat(count).isEqualTo(0);
        // БАГ!!! Думаю при фильтрации должно отображаться те транзакции, в которых участвовал залогиненный пользователь.
        // Здесь пользователь видит транзакции другого пользователя, в которой текущий пользователь не участвует!
    }
}
