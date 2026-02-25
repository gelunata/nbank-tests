package api.iteration2;

import api.BaseTest;
import api.dao.CountDao;
import api.dao.DepositDao;
import api.dao.comparison.DaoAndModelAssertions;
import api.models.AccountResponse;
import api.models.DepositResponse;
import api.requests.steps.AccountSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import api.requests.steps.DataBaseSteps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositTest extends BaseTest {
    @ValueSource(doubles = {0.01, 4999.99, 5000.0})
    @ParameterizedTest
    public void userCanDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();
        long id = AccountSteps.createAccount(userAuthorization)
                .getId();

        double balance = CustomerSteps.getBalance(userAuthorization, id);
        DepositResponse depositResponse = AccountSteps.depositMoney(userAuthorization, id, amount);

        softly.assertThat(CustomerSteps.getBalance(userAuthorization, id)).isEqualTo(balance + amount);

        DepositDao depositDao = DataBaseSteps.getTransactionById(depositResponse.getTransactionId());
        DaoAndModelAssertions.assertThat(depositResponse, depositDao).match();
    }

    @ValueSource(doubles = {-1.0, 0.0, 5000.01})
    @ParameterizedTest
    public void userCannotDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();
        long id = AccountSteps.createAccount(userAuthorization)
                .getId();
        CountDao transactionDaoExpected = DataBaseSteps.countTransactionByAccountId(id);

        double balance = CustomerSteps.getBalance(userAuthorization, id);
        AccountSteps.depositMoneyFailed(userAuthorization, id, amount);

        assertEquals(balance, CustomerSteps.getBalance(userAuthorization, id));

        CountDao transactionDaoActual = DataBaseSteps.countTransactionByAccountId(id);
        softly.assertThat(transactionDaoActual).isEqualTo(transactionDaoExpected);
    }

    @Test
    public void userCannotDepositMoneyToSomeoneOrToNonexistentAccountTest() {
        CountDao countRowsOfTransactionExpected = DataBaseSteps.countRowsOfTable(DataBaseSteps.Table.TRANSACTIONS);

        String userAuthorization = AdminSteps.createUser();
        AccountSteps.createAccount(userAuthorization);

        AccountResponse[] accounts = CustomerSteps.getAccounts(userAuthorization).extract().as(AccountResponse[].class);
        long maxId = Arrays.stream(accounts)
                .mapToLong(AccountResponse::getId)
                .max()
                .orElse(0);

        AccountSteps.depositMoneyForbidden(userAuthorization, ++maxId, 500);

        CountDao countRowsOfTransactionActual = DataBaseSteps.countRowsOfTable(DataBaseSteps.Table.TRANSACTIONS);
        softly.assertThat(countRowsOfTransactionActual).isEqualTo(countRowsOfTransactionExpected);
    }
}
