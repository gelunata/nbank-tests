package ui.iteration2;

import api.generators.RandomData;
import api.models.AccountResponse;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.DepositPage;
import ui.pages.UserDashboard;

import static org.assertj.core.api.Assertions.assertThat;

public class DepositTest extends BaseUiTest {
    @Test
    public void userCanDepositMoneyIntoHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse account = AccountsSteps.createAccount(userAuthHeader);

        authAsUser(userAuthHeader);

        new UserDashboard().open()
                .depositMoney()
                .getPage(DepositPage.class)
                .deposit(account.getAccountNumber(), amount)
                .checkAlertMessageAndAccept(
                        BankAlert.SUCCESSFULLY_DEPOSITED_TO_ACCOUNT.format(amount, account.getAccountNumber()));

        assertThat(CustomerSteps.getBalance(userAuthHeader, account.getId())).isEqualTo(amount);
    }

    @Test
    public void userCannotDepositMoneyIntoHisAccountTest() {
        double amount = RandomData.getIncorrectDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse account = AccountsSteps.createAccount(userAuthHeader);

        authAsUser(userAuthHeader);

        new DepositPage().open()
                .deposit(account.getAccountNumber(), amount)
                .checkAlertMessageAndAccept(BankAlert.PLEASE_DEPOSIT_LESS_OR_EQUAL_TO_5000.getMessage());

        assertThat(CustomerSteps.getBalance(userAuthHeader, account.getId())).isZero();
    }

    @Test
    public void userCannotDepositMoneyIfNoAccountIsSelectedTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountsSteps.createAccount(userAuthHeader);

        authAsUser(userAuthHeader);

        new DepositPage().open()
                .deposit(amount)
                .checkAlertMessageAndAccept(BankAlert.PLEASE_SELECT_AN_ACCOUNT.getMessage());
    }
}
