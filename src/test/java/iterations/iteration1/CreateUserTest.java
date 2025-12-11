package iterations.iteration1;

import iterations.BaseTest;
import models.CreateUserRequest;
import models.CreateUserResponse;
import models.comparision.ModelAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.stream.Stream;

public class CreateUserTest extends BaseTest {
    @ParameterizedTest
    @ValueSource(strings = {"abc", "123", "---", "___", "..."})
    public void adminCanCreateUserWithCorrectData(String username) {
        CreateUserRequest createUserRequest = AdminSteps.createUserRequest(username);
        CreateUserResponse createUserResponse = AdminSteps.createUser(createUserRequest);

        ModelAssertions.assertThatModels(createUserRequest, createUserResponse).match();
    }

    public static Stream<Arguments> userInvalidData() {
        return Stream.of(
                // username field validation
                Arguments.of("  ", "Password1!", "username",
                        "Username cannot be blank"),
                Arguments.of("ab", "Password1!", "username",
                        "Username must be between 3 and 15 characters"),
                Arguments.of("ab%", "Password1!", "username",
                        "Username must contain only letters, digits, dashes, underscores, and dots")
        );
    }

    @MethodSource("userInvalidData")
    @ParameterizedTest
    public void adminCanNotCreateUserWithInvalidData(String username, String password, String errorKey, String errorValue) {
        CreateUserRequest createUserRequest = AdminSteps.createUserRequest(username, password);

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsBadRequest(errorKey, errorValue))
                .post(createUserRequest);
    }
}
