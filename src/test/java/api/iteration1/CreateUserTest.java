package api.iteration1;

import api.BaseTest;
import api.models.CreateUserRequest;
import api.models.CreateUserResponse;
import api.models.comparision.ModelAssertions;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.steps.AdminSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
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
                        List.of("Username cannot be blank", "Username must be between 3 and 15 characters", "Username must contain only letters, digits, dashes, underscores, and dots")),
                Arguments.of("ab", "Password1!", "username",
                        List.of("Username must be between 3 and 15 characters")),
                Arguments.of("ab%", "Password1!", "username",
                        List.of("Username must contain only letters, digits, dashes, underscores, and dots"))
        );
    }

    @MethodSource("userInvalidData")
    @ParameterizedTest
    public void adminCanNotCreateUserWithInvalidData(String username, String password, String errorKey, List<String> errorValues) {
        CreateUserRequest createUserRequest = AdminSteps.createUserRequest(username, password);

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsBadRequest(errorKey, errorValues))
                .post(createUserRequest);
    }
}
