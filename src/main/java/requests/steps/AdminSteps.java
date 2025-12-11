package requests.steps;

import generators.RandomData;
import generators.RandomModelGenerator;
import models.CreateUserRequest;
import models.UserRole;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class AdminSteps {
    public static CreateUserRequest createUserRequest() {
        return RandomModelGenerator.generate(CreateUserRequest.class);
    }

    private static CreateUserRequest createUserRequest(String username, String password) {
        return CreateUserRequest.builder()
                .username(username)
                .password(password)
                .role(UserRole.USER.toString())
                .build();
    }

    public static String createUser() {
        CreateUserRequest userRequest = RandomModelGenerator.generate(CreateUserRequest.class);
        return createUser(userRequest);
    }

    public static String createUser(String username) {
        CreateUserRequest userRequest = createUserRequest(username, RandomData.getPassword());
        return createUser(userRequest);
    }

    public static String createUser(String username, String password) {
        CreateUserRequest userRequest = createUserRequest(username, password);
        return createUser(userRequest);
    }


    public static String createUser(CreateUserRequest userRequest) {
        return new CrudRequester(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(userRequest)
                .extract()
                .header(ResponseSpecs.AUTHORIZATION_HEADER);
    }
}
