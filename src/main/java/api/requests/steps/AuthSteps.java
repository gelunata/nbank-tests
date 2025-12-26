package api.requests.steps;

import io.restassured.response.ValidatableResponse;
import api.models.UserLoginRequest;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

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
