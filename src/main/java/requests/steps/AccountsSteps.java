package requests.steps;

import io.restassured.specification.ResponseSpecification;
import models.AccountResponse;
import models.DepositRequest;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class AccountsSteps {
    public static AccountResponse createAccount(String userAuthorization) {
        return new ValidatedCrudRequester<AccountResponse>(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);
    }

    public static void depositMoney(String userAuthorization, long id, double amount) {
        deposit(userAuthorization, id, amount, ResponseSpecs.requestReturnsOK());
    }

    public static void depositMoneyFailed(String userAuthorization, long id, double amount) {
        deposit(userAuthorization, id, amount, ResponseSpecs.requestReturnsBadRequest());
    }

    public static void depositMoneyForbidden(String userAuthorization, long id, double amount) {
        deposit(userAuthorization, id, amount, ResponseSpecs.requestReturnsForbidden());
    }

    private static void deposit(String userAuthorization, long id, double amount, ResponseSpecification responseSpecification) {
        DepositRequest depositRequest = DepositRequest.builder()
                .id(id)
                .balance(amount)
                .build();

        new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.ACCOUNTS_DEPOSIT,
                responseSpecification)
                .post(depositRequest);
    }
}
