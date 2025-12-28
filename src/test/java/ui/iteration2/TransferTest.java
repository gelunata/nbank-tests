package ui.iteration2;

import api.generators.RandomData;
import api.models.AccountResponse;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.TransferPage;
import ui.pages.UserDashboard;

public class TransferTest extends BaseUiTest {
    @Test
    public void userCanTransferMoneyBetweenHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);

        authAsUser(userAuthHeader);

        new UserDashboard().open()
                .makeTransfer()
                .getPage(TransferPage.class)
                .transfer(sender.getAccountNumber(), "", recipient.getAccountNumber(), amount, true)
                .checkAlertMessageAndAccept(BankAlert.SUCCESSFULLY_TRANSFERRED_TO_ACCOUNT.format(amount, recipient.getAccountNumber()));

        softly.assertThat(CustomerSteps.getBalance(userAuthHeader, sender.getId())).isZero();
        softly.assertThat(CustomerSteps.getBalance(userAuthHeader, recipient.getId())).isEqualTo(amount);
    }

    @Test
    public void userCannotTransferMoneyBetweenHisAccountTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);

        authAsUser(userAuthHeader);

        new TransferPage().open()
                .transfer(sender.getAccountNumber(), "", recipient.getAccountNumber(), amount + 0.01, true)
                .checkAlertMessageAndAccept(BankAlert.ERROR_INVALID_TRANSFER.format());

        softly.assertThat(CustomerSteps.getBalance(userAuthHeader, sender.getId())).isEqualTo(amount);
        softly.assertThat(CustomerSteps.getBalance(userAuthHeader, recipient.getId())).isZero();
    }

    @Test
    public void userCannotTransferMoneyIfAmountIsNotFilledTest() {
        double amount = RandomData.getDepositAmount();

        String userAuthHeader = AdminSteps.createUser();
        AccountResponse sender = AccountsSteps.createAccount(userAuthHeader);
        AccountResponse recipient = AccountsSteps.createAccount(userAuthHeader);
        AccountsSteps.depositMoney(userAuthHeader, sender.getId(), amount);

        authAsUser(userAuthHeader);

        new TransferPage().open()
                .transfer(sender.getAccountNumber(), "", recipient.getAccountNumber(), true)
                .checkAlertMessageAndAccept(BankAlert.PLEASE_FILL_ALL_FIELDS_AND_CONFIRM.getMessage());
    }
}
