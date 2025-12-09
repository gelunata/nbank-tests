package methods;

import generators.RandomData;
import models.CreateUserRequest;
import models.UserRole;
import requests.AdminCreateUserRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class User {
    public static String create() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        return new AdminCreateUserRequester(
                RequestSpecs.adminSpec(),
                ResponseSpecs.entityWasCreated())
                .post(createUserRequest)
                .extract()
                .header("Authorization");
    }
}
