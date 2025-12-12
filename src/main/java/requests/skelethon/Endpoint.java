package requests.skelethon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.*;

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
