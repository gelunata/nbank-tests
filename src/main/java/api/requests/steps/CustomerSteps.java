package api.requests.steps;

import api.models.AccountResponse;
import api.models.UpdateNameRequest;
import api.models.UserProfileResponse;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;

import java.util.Arrays;

public class CustomerSteps {
    public static ValidatableResponse getAccounts(String userAuthorization) {
        return new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_ACCOUNTS,
                ResponseSpecs.requestReturnsOK())
                .get(null);
    }

    public static double getBalance(String userAuthorization, long id) {
        ValidatableResponse response = getAccounts(userAuthorization);
        return Arrays.stream(response.extract().as(AccountResponse[].class))
                .filter(a -> a.getId() == id)
                .map(AccountResponse::getBalance)
                .findFirst()
                .orElseThrow();
    }

    public static boolean hasAccount(String userAuthorization, long id) {
        ValidatableResponse response = getAccounts(userAuthorization);
        return Arrays.stream(response.extract().as(AccountResponse[].class))
                .anyMatch(a -> a.getId() == id);
    }

    public static UserProfileResponse updateName(String userAuthorization, String newName) {
        return new ValidatedCrudRequester<UserProfileResponse>(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                ResponseSpecs.requestReturnsOK())
                .put(getUpdateNameRequest(newName));
    }

    public static void updateNameFailed(String userAuthorization, String newName) {
        new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                ResponseSpecs.requestReturnsBadRequest())
                .put(getUpdateNameRequest(newName));
    }

    private static UpdateNameRequest getUpdateNameRequest(String name) {
        return UpdateNameRequest.builder()
                .name(name)
                .build();
    }

    public static String getName(String userAuthorization) {
        return new ValidatedCrudRequester<UserProfileResponse>(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .getName();
    }
}
