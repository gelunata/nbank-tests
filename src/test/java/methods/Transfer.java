package methods;

import io.restassured.specification.ResponseSpecification;
import models.TransferRequest;
import requests.TransferMoneyRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class Transfer {
    private static void transferMoney(String userAuthorization, int senderId, int receiverId, double amount, ResponseSpecification response) {
        TransferRequest transferRequest = TransferRequest.builder()
                .senderAccountId(senderId)
                .receiverAccountId(receiverId)
                .amount(amount)
                .build();

        new TransferMoneyRequester(
                RequestSpecs.authAsUser(userAuthorization),
                response)
                .post(transferRequest);
    }

    public static void returnOK(String userAuthorization, int senderId, int receiverId, double amount) {
        transferMoney(userAuthorization, senderId, receiverId, amount, ResponseSpecs.requestReturnsOK());
    }

    public static void returnBadRequest(String userAuthorization, int senderId, int receiverId, double amount) {
        transferMoney(userAuthorization, senderId, receiverId, amount, ResponseSpecs.requestReturnsBadRequest());
    }
}
