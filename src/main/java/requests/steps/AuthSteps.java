package requests.steps;

import io.restassured.response.ValidatableResponse;
import models.UserLoginRequest;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class AuthSteps {
    public static UserLoginRequest getAuthRequest(String username, String password) {
        return UserLoginRequest.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static ValidatableResponse getAuth(UserLoginRequest request) {
        return new CrudRequester(
                RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(request);
    }
}
