package iterations.iteration2;

import models.AccountResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import requests.steps.AccountsSteps;
import requests.steps.AdminSteps;
import requests.steps.CustomerSteps;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositTest {
    @ValueSource(doubles = {0.01, 4999.99, 5000.0})
    @ParameterizedTest
    public void userCanDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();
        long id = AccountsSteps.createAccount(userAuthorization)
                .getId();

        double balance = CustomerSteps.getBalance(userAuthorization, id);
        AccountsSteps.depositMoney(userAuthorization, id, amount);

        assertEquals(balance + amount, CustomerSteps.getBalance(userAuthorization, id));
    }

    @ValueSource(doubles = {-1.0, 0.0, 5000.01})
    @ParameterizedTest
    public void userCannotDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();
        long id = AccountsSteps.createAccount(userAuthorization)
                .getId();

        double balance = CustomerSteps.getBalance(userAuthorization, id);
        AccountsSteps.depositMoneyFailed(userAuthorization, id, amount);

        assertEquals(balance, CustomerSteps.getBalance(userAuthorization, id));
    }

    @Test
    public void userCannotDepositMoneyToSomeoneOrToNonexistentAccountTest() {
        String userAuthorization = AdminSteps.createUser();
        AccountsSteps.createAccount(userAuthorization);

        AccountResponse[] accounts = CustomerSteps.getAccounts(userAuthorization).extract().as(AccountResponse[].class);
        long maxId = Arrays.stream(accounts)
                .mapToLong(AccountResponse::getId)
                .max()
                .orElse(0);

        AccountsSteps.depositMoneyForbidden(userAuthorization, ++maxId, 500);
    }
}
