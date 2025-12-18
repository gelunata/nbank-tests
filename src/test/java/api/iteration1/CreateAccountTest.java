package api.iteration1;

import org.junit.jupiter.api.Test;
import requests.steps.AccountsSteps;
import requests.steps.AdminSteps;
import requests.steps.CustomerSteps;

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
