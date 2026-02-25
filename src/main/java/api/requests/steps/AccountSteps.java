package api.requests.steps;

import api.models.*;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import common.helps.StepLogger;
import io.restassured.specification.ResponseSpecification;

public class AccountSteps {
    public static AccountResponse createAccount(String userAuthorization) {
        return new ValidatedCrudRequester<AccountResponse>(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);
    }

    public static DepositResponse depositMoney(String userAuthorization, long accountId, double amount) {
        return StepLogger.log("User deposits " + amount + " to account " + accountId, () -> {
            DepositRequest depositRequest = DepositRequest.builder()
                    .accountId(accountId)
                    .amount(amount)
                    .description("Test deposit")
                    .build();

            return new ValidatedCrudRequester<DepositResponse>(
                    RequestSpecs.authAsUser(userAuthorization),
                    Endpoint.ACCOUNTS_DEPOSIT,
                    ResponseSpecs.requestReturnsOK())
                    .post(depositRequest);
        });
    }

    public static void depositMoneyFailed(String userAuthorization, long id, double amount) {
        deposit(userAuthorization, id, amount, ResponseSpecs.requestReturnsBadRequest());
    }

    public static void depositMoneyForbidden(String userAuthorization, long id, double amount) {
        deposit(userAuthorization, id, amount, ResponseSpecs.requestReturnsForbidden());
    }

    private static void deposit(String userAuthorization, long accountId, double amount, ResponseSpecification responseSpecification) {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountId(accountId)
                .amount(amount)
                .build();

        new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.ACCOUNTS_DEPOSIT,
                responseSpecification)
                .post(depositRequest);
    }

    public static TransferResponse transferMoney(String userAuthorization, long senderId, long receiverId, double amount) {
        return StepLogger.log("User transfers " + amount + " to " + receiverId + " with fraud check", () -> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .senderAccountId(senderId)
                    .receiverAccountId(receiverId)
                    .amount(amount)
                    .description("Test transfer with fraud check")
                    .build();

            return new ValidatedCrudRequester<TransferResponse>(
                    RequestSpecs.authAsUser(userAuthorization),
                    Endpoint.ACCOUNTS_TRANSFER,
                    ResponseSpecs.requestReturnsOK())
                    .post(transferRequest);
        });
    }

    public static TransferResponse transferWithFraudCheck(String userAuthorization, Long senderAccountId, Long receiverAccountId, double amount) {
        return StepLogger.log("User transfers " + amount + " to " + receiverAccountId + " with fraud check", () -> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .senderAccountId(senderAccountId)
                    .receiverAccountId(receiverAccountId)
                    .amount(amount)
                    .description("Test transfer with fraud check")
                    .build();

            return new ValidatedCrudRequester<TransferResponse>(
                    RequestSpecs.authAsUser(userAuthorization),
                    Endpoint.TRANSFER_WITH_FRAUD_CHECK,
                    ResponseSpecs.requestReturnsOK()).post(transferRequest);
        });
    }

    public static void transferMoneyFailed(String userAuthorization, long senderId, long receiverId, double amount) {
        TransferRequest transferRequest = TransferRequest.builder()
                .senderAccountId(senderId)
                .receiverAccountId(receiverId)
                .amount(amount)
                .description("Test transfer with fraud check")
                .build();

        new CrudRequester(
                RequestSpecs.authAsUser(userAuthorization),
                Endpoint.ACCOUNTS_TRANSFER,
                ResponseSpecs.requestReturnsBadRequest())
                .post(transferRequest);
    }
}
