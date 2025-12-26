package api.requests.steps;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import api.models.AccountResponse;
import api.models.GetUserProfileResponse;
import api.models.UpdateNameRequest;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

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

    public static void updateName(String userAuthorization, String newName) {
        updateName(userAuthorization, newName, ResponseSpecs.requestReturnsOK());
    }

    public static void updateNameFailed(String userAuthorization, String newName) {
        updateName(userAuthorization, newName, ResponseSpecs.requestReturnsBadRequest());
    }

    private static void updateName(String userAuthorization, String newName, ResponseSpecification responseSpecification) {
        UpdateNameRequest updateNameRequest = UpdateNameRequest.builder()
                .name(newName)
                .build();

        new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                responseSpecification)
                .put(updateNameRequest);
    }

    public static String getName(String userAuthorization) {
        return new ValidatedCrudRequester<GetUserProfileResponse>(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.CUSTOMER_PROFILE,
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .getName();
    }
}
