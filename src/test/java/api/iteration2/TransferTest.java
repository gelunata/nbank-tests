package api.iteration2;

import api.BaseTest;
import api.dao.TransactionDao;
import api.generators.RandomData;
import api.requests.steps.AccountSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import api.requests.steps.DataBaseSteps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.within;

public class TransferTest extends BaseTest {
    @ValueSource(doubles = {0.01, 9999.99, 10000.0})
    @ParameterizedTest
    public void userCanTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();

        long senderId = AccountSteps.createAccount(userAuthorization).getId();
        long receiverId = AccountSteps.createAccount(userAuthorization).getId();

        depositRequiredAmount(userAuthorization, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization, receiverId);

        AccountSteps.transferMoney(userAuthorization, senderId, receiverId, amount);

        softly.assertThat(balance1 - amount).isCloseTo(CustomerSteps.getBalance(userAuthorization, senderId), within(1e-9));
        softly.assertThat(balance2 + amount).isCloseTo(CustomerSteps.getBalance(userAuthorization, receiverId), within(1e-9));

        TransactionDao transactionSenderDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(senderId, DataBaseSteps.TransferType.TRANSFER_OUT);
        TransactionDao transactionReceiverDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(receiverId, DataBaseSteps.TransferType.TRANSFER_IN);
        softly.assertThat(transactionSenderDao).isNotNull();
        softly.assertThat(transactionReceiverDao).isNotNull();
    }

    @ValueSource(doubles = {-1.0, 0.0, 10000.01})
    @ParameterizedTest
    public void userCannotTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();

        long senderId = AccountSteps.createAccount(userAuthorization).getId();
        long receiverId = AccountSteps.createAccount(userAuthorization).getId();

        depositRequiredAmount(userAuthorization, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization, receiverId);

        AccountSteps.transferMoneyFailed(userAuthorization, senderId, receiverId, amount);

        softly.assertThat(balance1).isEqualTo(CustomerSteps.getBalance(userAuthorization, senderId));
        softly.assertThat(balance2).isEqualTo(CustomerSteps.getBalance(userAuthorization, receiverId));

        TransactionDao transactionSenderDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(senderId, DataBaseSteps.TransferType.TRANSFER_OUT);
        TransactionDao transactionReceiverDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(receiverId, DataBaseSteps.TransferType.TRANSFER_IN);
        softly.assertThat(transactionSenderDao).isNull();
        softly.assertThat(transactionReceiverDao).isNull();
    }

    @Test
    public void userCanTransferMoneyToSomeonesAccountTest() {
        double amount = RandomData.getDepositAmount();
        String userAuthorization1 = AdminSteps.createUser();
        String userAuthorization2 = AdminSteps.createUser();

        long senderId = AccountSteps.createAccount(userAuthorization1).getId();
        long receiverId = AccountSteps.createAccount(userAuthorization2).getId();

        depositRequiredAmount(userAuthorization1, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization1, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization2, receiverId);

        AccountSteps.transferMoney(userAuthorization1, senderId, receiverId, amount);

        softly.assertThat(balance1 - amount).isCloseTo(CustomerSteps.getBalance(userAuthorization1, senderId), within(1e-9));
        softly.assertThat(balance2 + amount).isCloseTo(CustomerSteps.getBalance(userAuthorization2, receiverId), within(1e-9));

        TransactionDao transactionSenderDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(senderId, DataBaseSteps.TransferType.TRANSFER_OUT);
        TransactionDao transactionReceiverDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(receiverId, DataBaseSteps.TransferType.TRANSFER_IN);
        softly.assertThat(transactionSenderDao).isNotNull();
        softly.assertThat(transactionReceiverDao).isNotNull();


    }

    @Test
    public void userCannotTransferMoneyFromAccountThatIsLessThanAmountBeingTransferredTest() {
        double amount = RandomData.getDepositAmount();
        String userAuthorization = AdminSteps.createUser();

        long senderId = AccountSteps.createAccount(userAuthorization).getId();
        long receiverId = AccountSteps.createAccount(userAuthorization).getId();

        AccountSteps.depositMoney(userAuthorization, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization, receiverId);

        AccountSteps.transferMoneyFailed(userAuthorization, senderId, receiverId, amount + 0.01);

        softly.assertThat(balance1).isEqualTo(CustomerSteps.getBalance(userAuthorization, senderId));
        softly.assertThat(balance2).isEqualTo(CustomerSteps.getBalance(userAuthorization, receiverId));

        TransactionDao transactionSenderDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(senderId, DataBaseSteps.TransferType.TRANSFER_OUT);
        TransactionDao transactionReceiverDao = DataBaseSteps.getTransactionByAccountIdAndTransferType(receiverId, DataBaseSteps.TransferType.TRANSFER_IN);
        softly.assertThat(transactionSenderDao).isNull();
        softly.assertThat(transactionReceiverDao).isNull();
    }

    private void depositRequiredAmount(String userAuthorization, long id, double amount) {
        do {
            AccountSteps.depositMoney(userAuthorization, id, 5000);
            amount -= 5000;
        } while (amount > 0);
    }
}
