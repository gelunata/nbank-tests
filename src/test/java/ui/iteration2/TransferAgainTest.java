package ui.iteration2;

import api.models.AccountResponse;
import com.codeborne.selenide.Condition;
import common.annotations.Accounts;
import common.annotations.Deposit;
import common.annotations.Transfer;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.TransferPage;

import java.util.List;

import static com.codeborne.selenide.Condition.disabled;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferAgainTest extends BaseUiTest {
    @Test
    @UserSession
    @Accounts(value = 2)
    @Deposit
    @Transfer
    public void userCanTransferMoneyAgainTest() {
        List<AccountResponse> accounts = SessionStorage.getSteps().getAllAccounts();
        double amount = SessionStorage.getSteps().getAllAccounts().get(1).getBalance();

        new TransferPage().open()
                .transferAgain()
                .repeat()
                .repeatTransfer(accounts.get(1).getAccountNumber(), amount, true)
                .checkAlertMessageAndAccept(BankAlert.TRANSFER_SUCCESSFUL_FROM_ACCOUNT_TO_ACCOUNT.format(
                        amount, accounts.get(1).getId(), accounts.get(0).getId()));
        // БАГ в выводе сообщения!!! Сообщение пишет, что перевел из recipient.getId() в recipient.getId()

        // БАГ Сообщение об успешности, а на самом деле через API ничего не перевелось. Сумма осталась на счетах.
        softly.assertThat(SessionStorage.getSteps().getAllAccounts().get(0).getBalance()).isEqualTo(amount);
        softly.assertThat(SessionStorage.getSteps().getAllAccounts().get(1).getBalance()).isEqualTo(0.0);
    }

    @Test
    @UserSession
    @Accounts(value = 2)
    @Deposit
    @Transfer
    public void userCannotTransferMoneyAgainTest() {
        List<AccountResponse> accounts = SessionStorage.getSteps().getAllAccounts();
        double amount = SessionStorage.getSteps().getAllAccounts().get(1).getBalance();

        new TransferPage().open()
                .transferAgain()
                .repeat()
                .repeatTransfer(accounts.get(1).getAccountNumber(), amount + 0.01, true)
                .checkAlertMessageAndAccept(BankAlert.TRANSFER_FAILED_PLEASE_TRY_AGAIN.getMessage())
                .getRepeatTransferTitle()
                .shouldBe(Condition.visible);
    }

    @Test
    @UserSession
    @Accounts(value = 2)
    @Deposit
    @Transfer
    public void buttonSendTransferDisabledIfAmountIsNotFilledTest() {
        List<AccountResponse> accounts = SessionStorage.getSteps().getAllAccounts();

        new TransferPage().open()
                .transferAgain()
                .repeat()
                .fillValuesForRepeatTransfer(accounts.get(1).getAccountNumber(), true)
                .getTransferButton()
                .shouldBe(disabled);
        // !!! БАГ, ну или предположение, что при незаполненном поле Amount кнопка не должна быть активна,
        // как и при других случаях, когда одно из других полей не заполнено.
    }

    @Test
    @UserSession(value = 2)
    @Accounts
    @Deposit
    @Transfer(authRecipient = 2)
    public void filterIsWorkingCorrectly() {
        long count = new TransferPage().open()
                .transferAgain()
                .searchTransactionByUsernameOrName(SessionStorage.getUser(2).getUsername())
                .getMatchingTransactions()
                .stream().count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    @UserSession(value = 2)
    @Accounts
    @Deposit
    @Transfer(authRecipient = 2)
    public void filterIsWorkingCorrectlyIfNoMatches() {
        long count = new TransferPage().open()
                .transferAgain()
                .searchTransactionByUsernameOrName(SessionStorage.getUser(2).getUsername())
                .getMatchingTransactions()
                .stream().count();

        assertThat(count).isEqualTo(0);
        // БАГ!!! Думаю при фильтрации должно отображаться те транзакции, в которых участвовал залогиненный пользователь.
        // Здесь пользователь видит транзакции другого пользователя, в которой текущий пользователь не участвует!
    }
}
