package requests.steps;

import io.restassured.response.ValidatableResponse;
import models.GetUserProfileResponse;
import models.UpdateNameRequest;
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

    public static void updateName(String userAuthorization, String newName) {
        UpdateNameRequest updateNameRequest = UpdateNameRequest.builder()
                .name(newName)
                .build();

        new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                ResponseSpecs.requestReturnsOK())
                .put(updateNameRequest);
    }

    public static String getName(String userAuthorization) {
        return new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .extract()
                .as(GetUserProfileResponse.class)
                .getName();
    }
}
