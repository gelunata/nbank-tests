package methods;

import models.CreateAccountResponse;
import requests.CreateAccountRequester;
import requests.GetAccountsRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class Account {
    public static long create(String userAuthorization) {
        return new CreateAccountRequester(
                RequestSpecs.authAsUser(userAuthorization),
                ResponseSpecs.entityWasCreated())
                .post(null)
                .extract()
                .as(CreateAccountResponse.class)
                .getId();
    }

    public static double getBalance(String userAuthorization, long accountId) {
        return new GetAccountsRequester(
                RequestSpecs.authAsUser(userAuthorization),
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .extract()
                .jsonPath()
                .getDouble(String.format("find { it.id == %d }.balance", accountId));
    }
}
