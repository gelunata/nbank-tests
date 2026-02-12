package api.iteration1;

import api.dao.comparison.AccountDao;
import api.dao.comparison.DaoAndModelAssertions;
import api.models.AccountResponse;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import api.requests.steps.DataBaseSteps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAccountTest {

    @Test
    public void userCanCreateAccountTest() {
        String userAuthorization = AdminSteps.createUser();
        AccountResponse accountResponse = AccountsSteps.createAccount(userAuthorization);

        assertTrue(CustomerSteps.hasAccount(userAuthorization, accountResponse.getId()));

        AccountDao accountDao = DataBaseSteps.getAccountByAccountNumber(accountResponse.getAccountNumber());
        DaoAndModelAssertions.assertThat(accountResponse, accountDao).match();
    }
}
