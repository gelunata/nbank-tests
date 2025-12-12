package requests.steps;

import io.restassured.response.ValidatableResponse;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class AccountsSteps {
    public static ValidatableResponse createAccount(String userAuthorization){
        return new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);
    }
}
