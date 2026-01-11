package ui.iteration2;

import api.models.AccountResponse;
import common.annotations.Accounts;
import common.annotations.Deposit;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.TransferPage;
import ui.pages.UserDashboard;

import java.util.List;

public class TransferTest extends BaseUiTest {
    @Test
    @UserSession
    @Accounts(value = 2)
    @Deposit
    public void userCanTransferMoneyBetweenHisAccountTest() {
        List<AccountResponse> accounts = SessionStorage.getSteps().getAllAccounts();
        double amount = SessionStorage.getSteps().getAllAccounts().getFirst().getBalance();

        new UserDashboard().open()
                .makeTransfer()
                .getPage(TransferPage.class)
                .transfer(accounts.get(0).getAccountNumber(), "", accounts.get(1).getAccountNumber(), accounts.get(0).getBalance(), true)
                .checkAlertMessageAndAccept(BankAlert.SUCCESSFULLY_TRANSFERRED_TO_ACCOUNT.format(amount, accounts.get(1).getAccountNumber()));

        softly.assertThat(SessionStorage.getSteps().getAllAccounts().get(0).getBalance()).isZero();
        softly.assertThat(SessionStorage.getSteps().getAllAccounts().get(1).getBalance()).isEqualTo(amount);
    }

    @Test
    @UserSession
    @Accounts(value = 2)
    @Deposit
    public void userCannotTransferMoneyBetweenHisAccountTest() {
        List<AccountResponse> accounts = SessionStorage.getSteps().getAllAccounts();
        double amount = SessionStorage.getSteps().getAllAccounts().getFirst().getBalance();

        new TransferPage().open()
                .transfer(accounts.get(0).getAccountNumber(), "", accounts.get(1).getAccountNumber(), amount + 0.01, true)
                .checkAlertMessageAndAccept(BankAlert.ERROR_INVALID_TRANSFER.format());

        softly.assertThat(SessionStorage.getSteps().getAllAccounts().get(0).getBalance()).isEqualTo(amount);
        softly.assertThat(SessionStorage.getSteps().getAllAccounts().get(1).getBalance()).isZero();
    }

    @Test
    @UserSession
    @Accounts(value = 2)
    @Deposit
    public void userCannotTransferMoneyIfAmountIsNotFilledTest() {
        List<AccountResponse> accounts = SessionStorage.getSteps().getAllAccounts();

        new TransferPage().open()
                .transfer(accounts.get(0).getAccountNumber(), "", accounts.get(1).getAccountNumber(), true)
                .checkAlertMessageAndAccept(BankAlert.PLEASE_FILL_ALL_FIELDS_AND_CONFIRM.getMessage());
    }
}
