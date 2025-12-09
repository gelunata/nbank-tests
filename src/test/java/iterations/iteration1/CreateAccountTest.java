package iterations.iteration1;

import generators.RandomData;
import models.CreateUserRequest;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.CreateAccountRequester;
import requests.GetAccountsRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import static org.hamcrest.Matchers.hasItem;

public class CreateAccountTest {

    @Test
    public void userCanCreateAccountTest() {
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(
                RequestSpecs.adminSpec(),
                ResponseSpecs.entityWasCreated())
                .post(userRequest);

        int id = new CreateAccountRequester(
                RequestSpecs.authAsUser(
                        userRequest.getUsername(),
                        userRequest.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post(null)
                .extract()
                .path("id");

        new GetAccountsRequester(
                RequestSpecs.authAsUser(
                        userRequest.getUsername(),
                        userRequest.getPassword()),
                ResponseSpecs.requestReturnsOK())
                .get(null)
                .body("id", hasItem(id));
    }
}
