package methods;

import io.restassured.specification.ResponseSpecification;
import models.DepositRequest;
import requests.DepositMoneyRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class Deposit {
    private static void depositMoney(String userAuthorization, int accountId, double amount, ResponseSpecification response) {
        DepositRequest depositRequest = DepositRequest.builder()
                .id(accountId)
                .balance(amount)
                .build();

        new DepositMoneyRequester(
                RequestSpecs.authAsUser(userAuthorization),
                response)
                .post(depositRequest);
    }

    public static void returnsOK(String userAuthorization, int accountId, double amount) {
        depositMoney(userAuthorization, accountId, amount, ResponseSpecs.requestReturnsOK());
    }

    public static void returnsBadRequest(String userAuthorization, int accountId, double amount) {
        depositMoney(userAuthorization, accountId, amount, ResponseSpecs.requestReturnsBadRequest());
    }

    public static void returnsForbidden(String userAuthorization, int accountId, double amount) {
        depositMoney(userAuthorization, accountId, amount, ResponseSpecs.requestReturnsForbidden());
    }
}
