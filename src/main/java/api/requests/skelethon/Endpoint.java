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
            TransferRequest.class,
            TransferResponse.class
    ),

    TRANSFER_WITH_FRAUD_CHECK(
            "/accounts/transfer-with-fraud-check",
            TransferRequest.class,
            TransferResponse.class
    ),

    FRAUD_CHECK_STATUS(
            "/api/v1/accounts/fraud-check/{transactionId}",
            BaseModel.class,
            FraudCheckResponse.class
    ),

    ACCOUNTS_DEPOSIT(
            "/accounts/deposit",
            DepositRequest.class,
            DepositResponse.class
    ),

    CUSTOMER_PROFILE(
            "/customer/profile",
            BaseModel.class,
            UserProfileResponse.class

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
