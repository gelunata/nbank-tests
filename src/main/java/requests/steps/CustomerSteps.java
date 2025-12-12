package requests.steps;

import io.restassured.response.ValidatableResponse;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class CustomerSteps {
    public static ValidatableResponse getAccounts(String userAuthorization) {
        return new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_ACCOUNTS,
                ResponseSpecs.requestReturnsOK())
                .get(null);
    }
}
