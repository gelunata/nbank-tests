package requests.steps;

import generators.RandomData;
import generators.RandomModelGenerator;
import models.CreateUserRequest;
import models.CreateUserResponse;
import models.UserRole;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

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
}
