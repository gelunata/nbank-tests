package api.requests.steps;

import api.helpers.StepLogger;
import api.models.AccountResponse;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import java.util.Comparator;
import java.util.List;

public class UserSteps {
    private String username;
    private String password;

    public UserSteps(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public List<AccountResponse> getAllAccounts() {
        return StepLogger.log("User " + username + " get all accounts", () -> {
            List<AccountResponse> accounts = new ValidatedCrudRequester<AccountResponse>(
                    RequestSpecs.authAsUser(username, password),
                    Endpoint.CUSTOMER_ACCOUNTS,
                    ResponseSpecs.requestReturnsOK())
                    .getAll(AccountResponse[].class);
            accounts.sort(Comparator.comparing(AccountResponse::getId));
            return accounts;
        });
    }
}
