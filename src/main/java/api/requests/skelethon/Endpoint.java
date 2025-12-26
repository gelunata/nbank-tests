package api.requests.skelethon;

import api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Endpoint {
    ADMIN_USER(
            "/admin/users",
            CreateUserRequest.class,
            CreateUserResponse.class
    ),

    LOGIN(
            "/auth/login",
            UserLoginRequest.class,
            UserLoginResponse.class
    ),

    ACCOUNTS(
            "/accounts",
            BaseModel.class,
            AccountResponse.class
    ),

    ACCOUNTS_TRANSFER(
            "/accounts/transfer",
            BaseModel.class,
            AccountResponse.class
    ),

    ACCOUNTS_DEPOSIT(
            "/accounts/deposit",
            DepositRequest.class,
            DepositResponse.class
    ),

    CUSTOMER_PROFILE(
            "/customer/profile",
            BaseModel.class,
            GetUserProfileResponse.class

    ),

    CUSTOMER_ACCOUNTS(
            "/customer/accounts",
            BaseModel.class,
            AccountResponse.class

    );

    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
