package mock;

import api.BaseTest;
import api.models.AccountResponse;
import api.models.DepositResponse;
import api.models.TransferResponse;
import api.models.comparision.ModelAssertions;
import api.requests.steps.AccountSteps;
import api.requests.steps.AdminSteps;
import common.annotations.FraudCheckMock;
import common.extensions.TimingExtension;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({TimingExtension.class, FraudCheckWireMockExtension.class})
public class TransferWithFraudCheckTest extends BaseTest {

    private String authUser1;
    private String authUser2;
    private AccountResponse account1;
    private AccountResponse account2;
    private DepositResponse depositResponse;
    private TransferResponse transferResponse;

    @BeforeEach
    public void setupTest() {
        this.softly = new SoftAssertions();
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void testTransferWithFraudCheck() {
        authUser1 = AdminSteps.createUser();

        account1 = AccountSteps.createAccount(authUser1);

        double depositAmount = Math.random() * 4999.9 + 0.1;
        depositResponse = AccountSteps.depositMoney(authUser1, account1.getId(), depositAmount);

        authUser2 = AdminSteps.createUser();
        account2 = AccountSteps.createAccount(authUser2);

        double transferAmount = Math.random() * (depositAmount - 0.1) + 0.1;
        transferResponse = AccountSteps.transferWithFraudCheck(authUser1,
                account1.getId(),
                account2.getId(),
                transferAmount
        );

        softly.assertThat(transferResponse).isNotNull();


        TransferResponse expectedResponse = TransferResponse.builder()
                .status("APPROVED")
                .message("Transfer approved and processed immediately")
                .amount(transferAmount)
                .senderAccountId(account1.getId())
                .receiverAccountId(account2.getId())
                .fraudRiskScore(0.2)
                .fraudReason("Low risk transaction")
                .requiresManualReview(false)
                .requiresVerification(false)
                .build();

        ModelAssertions.assertThatModels(expectedResponse, transferResponse).match();
    }

    @AfterEach
    public void afterTest() {
        softly.assertAll();
    }
}
