package api.requests.steps;

import api.models.UserLoginRequest;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;

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
