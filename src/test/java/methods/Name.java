package methods;

import io.restassured.specification.ResponseSpecification;
import models.GetUserProfileResponse;
import models.UpdateNameRequest;
import requests.GetProfileDetails;
import requests.UpdateNameRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class Name {
    private static void updateName(String userAuthorization, String name, ResponseSpecification response) {
        UpdateNameRequest updateNameRequest = UpdateNameRequest.builder()
                .name(name)
                .build();

        new UpdateNameRequester(
                RequestSpecs.authAsUser(userAuthorization),
                response)
                .put(updateNameRequest);
    }

    public static void updateRequestReturnBadRequest(String userAuthorization, String name) {
        updateName(userAuthorization, name, ResponseSpecs.requestReturnsBadRequest());
    }

    public static void updateRequestReturnOK(String userAuthorization, String name) {
        updateName(userAuthorization, name, ResponseSpecs.requestReturnsOK());
    }

    public static String get(String userAuthorization) {
        return new GetProfileDetails(
                RequestSpecs.authAsUser(userAuthorization),
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .extract()
                .as(GetUserProfileResponse.class)
                .getName();
    }
}
