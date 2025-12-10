package iterations.iteration2;

import methods.Account;
import methods.Deposit;
import methods.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import requests.GetAccountsRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositTest {
    @ValueSource(doubles = {0.01, 4999.99, 5000.0})
    @ParameterizedTest
    public void userCanDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = User.create();
        long id = Account.create(userAuthorization);
        double balance = Account.getBalance(userAuthorization, id);
        Deposit.returnsOK(userAuthorization, id, amount);

        assertEquals(balance + amount, Account.getBalance(userAuthorization, id));
    }

    @ValueSource(doubles = {-1.0, 0.0, 5000.01})
    @ParameterizedTest
    public void userCannotDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = User.create();
        long id = Account.create(userAuthorization);
        double balance = Account.getBalance(userAuthorization, id);

        Deposit.returnsBadRequest(userAuthorization, id, amount);

        assertEquals(balance, Account.getBalance(userAuthorization, id));
    }

    @Test
    public void userCannotDepositMoneyToSomeoneOrToNonexistentAccountTest() {
        String userAuthorization = User.create();
        Account.create(userAuthorization);

        long maxId = new GetAccountsRequester(
                RequestSpecs.authAsUser(userAuthorization),
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .extract()
                .jsonPath()
                .getList("id", Integer.class)
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        Deposit.returnsForbidden(userAuthorization, ++maxId, 500);
    }
}
