package iteration2;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest extends BaseTest {
    @ValueSource(doubles = {0.01, 9999.99, 10000.0})
    @ParameterizedTest
    public void userCanTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = createUser();

        int id1 = createAccount(userAuthorization);
        int id2 = createAccount(userAuthorization);

        double necessaryBalance = amount;
        do {
            depositMoney(userAuthorization, id1, 5000, HttpStatus.SC_OK);
            necessaryBalance -= 5000;
        } while (necessaryBalance > 0);

        double balance1 = getBalance(userAuthorization, id1);
        double balance2 = getBalance(userAuthorization, id2);

        transferMoney(userAuthorization, id1, id2, amount, HttpStatus.SC_OK);

        assertEquals(balance1 - amount, getBalance(userAuthorization, id1), 1e-9);
        assertEquals(balance2 + amount, getBalance(userAuthorization, id2), 1e-9);
    }

    @ValueSource(doubles = {-1.0, 0.0, 10000.01})
    @ParameterizedTest
    public void userCannotTransferMoneyBetweenHisAccountTest(double amount) {
        String userAuthorization = createUser();

        int id1 = createAccount(userAuthorization);
        int id2 = createAccount(userAuthorization);

        double necessaryBalance = amount;
        do {
            depositMoney(userAuthorization, id1, 5000, HttpStatus.SC_OK);
            necessaryBalance -= 5000;
        } while (necessaryBalance > 0);

        double balance1 = getBalance(userAuthorization, id1);
        double balance2 = getBalance(userAuthorization, id2);

        transferMoney(userAuthorization, id1, id2, amount, HttpStatus.SC_BAD_REQUEST);

        assertEquals(balance1, getBalance(userAuthorization, id1));
        assertEquals(balance2, getBalance(userAuthorization, id2));
    }

    @Test
    public void userCanTransferMoneyToSomeonesAccountTest() {
        double amount = 5000;
        String userAuthorization1 = createUser();
        String userAuthorization2 = createUser();

        int id1 = createAccount(userAuthorization1);
        int id2 = createAccount(userAuthorization2);

        depositMoney(userAuthorization1, id1, amount, HttpStatus.SC_OK);

        double balance1 = getBalance(userAuthorization1, id1);
        double balance2 = getBalance(userAuthorization2, id2);

        transferMoney(userAuthorization1, id1, id2, amount, HttpStatus.SC_OK);

        assertEquals(balance1 - amount, getBalance(userAuthorization1, id1));
        assertEquals(balance2 + amount, getBalance(userAuthorization2, id2));
    }

    @Test
    public void userCannotTransferMoneyFromAccountThatIsLessThanAmountBeingTransferredTest() {
        double amount = 5000;
        String userAuthorization = createUser();

        int id1 = createAccount(userAuthorization);
        int id2 = createAccount(userAuthorization);

        depositMoney(userAuthorization, id1, amount, HttpStatus.SC_OK);

        double balance1 = getBalance(userAuthorization, id1);
        double balance2 = getBalance(userAuthorization, id2);

        transferMoney(userAuthorization, id1, id2, amount + 0.01, HttpStatus.SC_BAD_REQUEST);

        assertEquals(balance1, getBalance(userAuthorization, id1));
        assertEquals(balance2, getBalance(userAuthorization, id2));
    }

    private void transferMoney(String userAuthorization, int id1, int id2, double amount, int httpStatus) {
        given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format(Locale.US, """
                        {
                            "senderAccountId": %d,
                            "receiverAccountId": %d,
                            "amount": %.2f
                        }
                        """, id1, id2, amount))
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .assertThat()
                .statusCode(httpStatus);
    }
}
