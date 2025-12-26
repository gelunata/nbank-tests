package api.iteration2;

import api.generators.RandomData;
import api.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import api.requests.steps.AccountsSteps;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;

import static org.assertj.core.api.AssertionsForClassTypes.within;

public class TransferTest extends BaseTest {
    @ValueSource(doubles = {0.01, 9999.99, 10000.0})
    @ParameterizedTest
    public void userCanTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();

        long senderId = AccountsSteps.createAccount(userAuthorization).getId();
        long receiverId = AccountsSteps.createAccount(userAuthorization).getId();

        depositRequiredAmount(userAuthorization, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization, receiverId);

        AccountsSteps.transferMoney(userAuthorization, senderId, receiverId, amount);

        softly.assertThat(balance1 - amount).isCloseTo(CustomerSteps.getBalance(userAuthorization, senderId), within(1e-9));
        softly.assertThat(balance2 + amount).isCloseTo(CustomerSteps.getBalance(userAuthorization, receiverId), within(1e-9));
    }

    @ValueSource(doubles = {-1.0, 0.0, 10000.01})
    @ParameterizedTest
    public void userCannotTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();

        long senderId = AccountsSteps.createAccount(userAuthorization).getId();
        long receiverId = AccountsSteps.createAccount(userAuthorization).getId();

        depositRequiredAmount(userAuthorization, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization, receiverId);

        AccountsSteps.transferMoneyFailed(userAuthorization, senderId, receiverId, amount);

        softly.assertThat(balance1).isEqualTo(CustomerSteps.getBalance(userAuthorization, senderId));
        softly.assertThat(balance2).isEqualTo(CustomerSteps.getBalance(userAuthorization, receiverId));
    }

    @Test
    public void userCanTransferMoneyToSomeonesAccountTest() {
        double amount = RandomData.getDepositAmount();
        String userAuthorization1 = AdminSteps.createUser();
        String userAuthorization2 = AdminSteps.createUser();

        long senderId = AccountsSteps.createAccount(userAuthorization1).getId();
        long receiverId = AccountsSteps.createAccount(userAuthorization2).getId();

        depositRequiredAmount(userAuthorization1, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization1, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization2, receiverId);

        AccountsSteps.transferMoney(userAuthorization1, senderId, receiverId, amount);

        softly.assertThat(balance1 - amount).isEqualTo(CustomerSteps.getBalance(userAuthorization1, senderId));
        softly.assertThat(balance2 + amount).isEqualTo(CustomerSteps.getBalance(userAuthorization2, receiverId));
    }

    @Test
    public void userCannotTransferMoneyFromAccountThatIsLessThanAmountBeingTransferredTest() {
        double amount = RandomData.getDepositAmount();
        String userAuthorization = AdminSteps.createUser();

        long senderId = AccountsSteps.createAccount(userAuthorization).getId();
        long receiverId = AccountsSteps.createAccount(userAuthorization).getId();

        AccountsSteps.depositMoney(userAuthorization, senderId, amount);

        double balance1 = CustomerSteps.getBalance(userAuthorization, senderId);
        double balance2 = CustomerSteps.getBalance(userAuthorization, receiverId);

        AccountsSteps.transferMoneyFailed(userAuthorization, senderId, receiverId, amount + 0.01);

        softly.assertThat(balance1).isEqualTo(CustomerSteps.getBalance(userAuthorization, senderId));
        softly.assertThat(balance2).isEqualTo(CustomerSteps.getBalance(userAuthorization, receiverId));
    }

    private void depositRequiredAmount(String userAuthorization, long id, double amount) {
        do {
            AccountsSteps.depositMoney(userAuthorization, id, 5000);
            amount -= 5000;
        } while (amount > 0);
    }
}
