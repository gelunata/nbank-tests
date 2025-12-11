package iterations.iteration2;

import generators.RandomData;
import iterations.BaseTest;
import methods.Account;
import methods.Deposit;
import methods.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import requests.steps.AdminSteps;

import static org.assertj.core.api.AssertionsForClassTypes.within;

public class TransferTest extends BaseTest {
    @ValueSource(doubles = {0.01, 9999.99, 10000.0})
    @ParameterizedTest
    public void userCanTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();

        long senderId = Account.create(userAuthorization);
        long receiverId = Account.create(userAuthorization);

        depositRequiredAmount(userAuthorization, senderId, amount);

        double balance1 = Account.getBalance(userAuthorization, senderId);
        double balance2 = Account.getBalance(userAuthorization, receiverId);

        Transfer.returnOK(userAuthorization, senderId, receiverId, amount);

        softly.assertThat(balance1 - amount).isCloseTo(Account.getBalance(userAuthorization, senderId), within(1e-9));
        softly.assertThat(balance2 + amount).isCloseTo(Account.getBalance(userAuthorization, receiverId), within(1e-9));
    }

    @ValueSource(doubles = {-1.0, 0.0, 10000.01})
    @ParameterizedTest
    public void userCannotTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = AdminSteps.createUser();

        long senderId = Account.create(userAuthorization);
        long receiverId = Account.create(userAuthorization);

        depositRequiredAmount(userAuthorization, senderId, amount);

        double balance1 = Account.getBalance(userAuthorization, senderId);
        double balance2 = Account.getBalance(userAuthorization, receiverId);

        Transfer.returnBadRequest(userAuthorization, senderId, receiverId, amount);

        softly.assertThat(balance1).isEqualTo(Account.getBalance(userAuthorization, senderId));
        softly.assertThat(balance2).isEqualTo(Account.getBalance(userAuthorization, receiverId));
    }

    @Test
    public void userCanTransferMoneyToSomeonesAccountTest() {
        double amount = RandomData.getDepositAmount();
        String userAuthorization1 = AdminSteps.createUser();
        String userAuthorization2 = AdminSteps.createUser();

        long senderId = Account.create(userAuthorization1);
        long receiverId = Account.create(userAuthorization2);

        Deposit.returnsOK(userAuthorization1, senderId, amount);

        double balance1 = Account.getBalance(userAuthorization1, senderId);
        double balance2 = Account.getBalance(userAuthorization2, receiverId);

        Transfer.returnOK(userAuthorization1, senderId, receiverId, amount);

        softly.assertThat(balance1 - amount).isEqualTo(Account.getBalance(userAuthorization1, senderId));
        softly.assertThat(balance2 + amount).isEqualTo(Account.getBalance(userAuthorization2, receiverId));
    }

    @Test
    public void userCannotTransferMoneyFromAccountThatIsLessThanAmountBeingTransferredTest() {
        double amount = RandomData.getDepositAmount();
        String userAuthorization = AdminSteps.createUser();

        long senderId = Account.create(userAuthorization);
        long receiverId = Account.create(userAuthorization);

        Deposit.returnsOK(userAuthorization, senderId, amount);

        double balance1 = Account.getBalance(userAuthorization, senderId);
        double balance2 = Account.getBalance(userAuthorization, receiverId);

        Transfer.returnBadRequest(userAuthorization, senderId, receiverId, amount + 0.01);

        softly.assertThat(balance1).isEqualTo(Account.getBalance(userAuthorization, senderId));
        softly.assertThat(balance2).isEqualTo(Account.getBalance(userAuthorization, receiverId));
    }

    private void depositRequiredAmount(String userAuthorization, long senderId, double amount) {
        do {
            Deposit.returnsOK(userAuthorization, senderId, 5000);
            amount -= 5000;
        } while (amount > 0);
    }
}
