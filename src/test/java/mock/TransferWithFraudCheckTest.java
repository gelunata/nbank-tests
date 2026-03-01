package mock;

import api.BaseTest;
import api.generators.RandomData;
import api.models.AccountResponse;
import api.models.TransferResponse;
import api.models.comparision.ModelAssertions;
import api.requests.steps.AccountSteps;
import common.annotations.Accounts;
import common.annotations.Deposit;
import common.annotations.FraudCheckMock;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

@ExtendWith(FraudCheckWireMockExtension.class)
public class TransferWithFraudCheckTest extends BaseTest {
    @BeforeEach
    public void setupTest() {
        this.softly = new SoftAssertions();
    }

    @Test
    @FraudCheckMock
    @UserSession(value = 2, auth = 0)
    @Accounts
    @Deposit
    public void userCanTransferMoneyToSomeonesAccountWithFraudCheckTest() {
        AccountResponse accountUser1 = SessionStorage.getSteps(1).getAllAccounts().getFirst();
        long accountIdByUser1 = accountUser1.getId();
        double accountBalanceByUser1 = accountUser1.getBalance();

        long accountIdByUser2 = SessionStorage.getSteps(2).getAllAccounts().getFirst().getId();

        double transferAmount = RandomData.getMoneyAmount(accountBalanceByUser1);

        TransferResponse transferResponse = AccountSteps.transferWithFraudCheck(
                SessionStorage.getUser().getUsername(), SessionStorage.getUser().getPassword(),
                accountIdByUser1, accountIdByUser2, transferAmount);

        softly.assertThat(transferResponse).isNotNull();

        TransferResponse expectedResponse = expectedResponse(transferAmount, accountIdByUser1, accountIdByUser2);
        ModelAssertions.assertThatModels(expectedResponse, transferResponse).match();
    }

    @Test
    @FraudCheckMock
    @UserSession(auth = 0)
    @Accounts(2)
    @Deposit
    public void userCannotTransferMoneyFromAccountThatIsLessThanAmountBeingTransferredWithFraudCheckTest() {
        List<AccountResponse> accountUser = SessionStorage.getSteps(1).getAllAccounts();
        long accountId1 = accountUser.getFirst().getId();
        double accountBalanceById1 = accountUser.getFirst().getBalance();
        long accountId2 = accountUser.getLast().getId();

        double transferAmount = RandomData.getTransferAmount(accountBalanceById1 + 0.01);

        AccountSteps.transferWithFraudCheckFailed(
                SessionStorage.getUser().getUsername(), SessionStorage.getUser().getPassword(),
                accountId1, accountId2, transferAmount);
    }


    @ValueSource(doubles = {0.01, 9999.99, 10000.0})
    @ParameterizedTest
    @FraudCheckMock
    @UserSession(auth = 0)
    @Accounts(2)
    @Deposit(10000.0)
    public void userCanTransferMoneyBetweenHisAccountWithFraudCheckTest(double amount) {
        List<AccountResponse> accountUser = SessionStorage.getSteps(1).getAllAccounts();
        long accountId1 = accountUser.getFirst().getId();
        long accountId2 = accountUser.getLast().getId();

        TransferResponse transferResponse = AccountSteps.transferWithFraudCheck(SessionStorage.getUser().getUsername(), SessionStorage.getUser().getPassword(),
                accountId1, accountId2, amount);

        softly.assertThat(transferResponse).isNotNull();

        TransferResponse expectedResponse = expectedResponse(amount, accountId1, accountId2);
        ModelAssertions.assertThatModels(expectedResponse, transferResponse).match();
    }

    @ValueSource(doubles = {-1.0, 0.0, 10000.01})
    @ParameterizedTest
    @FraudCheckMock
    @UserSession(auth = 0)
    @Accounts(2)
    @Deposit(11000.0)
    public void userCannotTransferMoneyBetweenHisAccountWithFraudCheckTest(double amount) {
        List<AccountResponse> accountUser = SessionStorage.getSteps(1).getAllAccounts();
        long accountId1 = accountUser.getFirst().getId();
        long accountId2 = accountUser.getLast().getId();

        AccountSteps.transferWithFraudCheckFailed(
                SessionStorage.getUser().getUsername(), SessionStorage.getUser().getPassword(),
                accountId1, accountId2, amount);
    }


    private TransferResponse expectedResponse(double transferAmount, long account1, long account2) {
        return TransferResponse.builder()
                .status("APPROVED")
                .message("Transfer approved and processed immediately")
                .amount(transferAmount)
                .senderAccountId(account1)
                .receiverAccountId(account2)
                .fraudRiskScore(0.2)
                .fraudReason("Low risk transaction")
                .requiresManualReview(false)
                .requiresVerification(false)
                .build();
    }

    @AfterEach
    public void afterTest() {
        softly.assertAll();
    }
}
