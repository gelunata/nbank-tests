package api.iteration1;

import models.CreateUserRequest;
import models.UserLoginRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import requests.steps.AdminSteps;
import requests.steps.AuthSteps;

public class LoginUserTest {
    @Test
    public void adminCanGenerateAuthTokenTest() {
        UserLoginRequest userLoginRequest = AuthSteps.getAuthRequest("admin", "admin");

        AuthSteps.getAuth(userLoginRequest)
                .header("Authorization", Matchers.notNullValue());
    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequest createUserRequest = AdminSteps.createUserRequest();
        AdminSteps.createUser(createUserRequest);
        UserLoginRequest userLoginResponse = new UserLoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());

        AuthSteps.getAuth(userLoginResponse)
                .header("Authorization", Matchers.notNullValue());
    }
}
