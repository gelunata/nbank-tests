package api.requests.steps;

import api.generators.RandomData;
import api.generators.RandomModelGenerator;
import api.models.CreateUserRequest;
import api.models.CreateUserResponse;
import api.models.UserRole;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import java.util.List;

public class AdminSteps {
    public static CreateUserRequest createUserRequest() {
        return RandomModelGenerator.generate(CreateUserRequest.class);
    }

    public static CreateUserRequest createUserRequest(String username) {
        return createUserRequest(username, RandomData.getPassword());
    }

    public static CreateUserRequest createUserRequest(String username, String password) {
        return CreateUserRequest.builder()
                .username(username)
                .password(password)
                .role(UserRole.USER.toString())
                .build();
    }

    public static String createUser() {
        CreateUserRequest userRequest = createUserRequest();
        return createUser(userRequest.getUsername(), userRequest.getPassword());
    }

    public static String createUser(String username, String password) {
        CreateUserRequest userRequest = createUserRequest(username, password);
        return new CrudRequester(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(userRequest)
                .extract()
                .header(ResponseSpecs.AUTHORIZATION_HEADER);
    }

    public static CreateUserResponse createUser(CreateUserRequest userRequest) {
        return new ValidatedCrudRequester<CreateUserResponse>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(userRequest);
    }

    public static List<CreateUserResponse> getAllUsers() {
        return new ValidatedCrudRequester<CreateUserResponse>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsOK())
                .getAll(CreateUserResponse[].class);
    }
}
