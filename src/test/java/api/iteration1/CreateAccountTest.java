package api.iteration1;

import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAccountTest {

    @Test
    public void userCanCreateAccountTest() {
        String userAuthorization = AdminSteps.createUser();

        long id = AccountsSteps.createAccount(userAuthorization)
                .getId();

        assertTrue(CustomerSteps.hasAccount(userAuthorization, id));
    }
}
