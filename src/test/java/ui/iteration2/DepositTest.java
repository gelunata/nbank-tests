package ui.iteration2;

import api.generators.RandomData;
import api.models.AccountResponse;
import common.annotations.Accounts;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.DepositPage;
import ui.pages.UserDashboard;

import static org.assertj.core.api.Assertions.assertThat;

public class DepositTest extends BaseUiTest {
    @Test
    @UserSession
    @Accounts
    public void userCanDepositMoneyIntoHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        AccountResponse account = SessionStorage.getSteps().getAllAccounts().getFirst();

        new UserDashboard().open()
                .depositMoney()
                .getPage(DepositPage.class)
                .deposit(account.getAccountNumber(), amount)
                .checkAlertMessageAndAccept(
                        BankAlert.SUCCESSFULLY_DEPOSITED_TO_ACCOUNT.format(amount, account.getAccountNumber()));

        assertThat(SessionStorage.getSteps().getAllAccounts().getFirst().getBalance()).isEqualTo(amount);
    }

    @Test
    @UserSession
    @Accounts
    public void userCannotDepositMoneyIntoHisAccountTest() {
        double amount = RandomData.getIncorrectDepositAmount();

        AccountResponse account = SessionStorage.getSteps().getAllAccounts().getFirst();

        new DepositPage().open()
                .deposit(account.getAccountNumber(), amount)
                .checkAlertMessageAndAccept(BankAlert.PLEASE_DEPOSIT_LESS_OR_EQUAL_TO_5000.getMessage());

        assertThat(SessionStorage.getSteps().getAllAccounts().getFirst().getBalance()).isZero();
    }

    @Test
    @UserSession
    @Accounts
    public void userCannotDepositMoneyIfNoAccountIsSelectedTest() {
        double amount = RandomData.getDepositAmount();

        new DepositPage().open()
                .deposit(amount)
                .checkAlertMessageAndAccept(BankAlert.PLEASE_SELECT_AN_ACCOUNT.getMessage());
    }
}
