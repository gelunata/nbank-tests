package iterations.iteration1;

import models.CreateAccountResponse;
import org.junit.jupiter.api.Test;
import requests.steps.AccountsSteps;
import requests.steps.AdminSteps;
import requests.steps.CustomerSteps;

import static org.hamcrest.Matchers.hasItem;

public class CreateAccountTest {

    @Test
    public void userCanCreateAccountTest() {
        String userAuthorization = AdminSteps.createUser();

        long id = AccountsSteps.createAccount(userAuthorization)
                .extract()
                .as(CreateAccountResponse.class)
                .getId();

        CustomerSteps.getAccounts(userAuthorization)
                .body("id", hasItem((int) id));
    }
}
