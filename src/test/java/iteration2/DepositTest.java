package iteration2;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositTest extends BaseTest {
    @ValueSource(doubles = {0.01, 4999.99, 5000.0})
    @ParameterizedTest
    public void userCanDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = createUser();
        int id = createAccount(userAuthorization);
        double balance = getBalance(userAuthorization, id);
        depositMoney(userAuthorization, id, amount, HttpStatus.SC_OK);

        assertEquals(balance + amount, getBalance(userAuthorization, id));
    }

    @ValueSource(doubles = {-1.0, 0.0, 5000.01})
    @ParameterizedTest
    public void userCannotDepositMoneyIntoHisAccountTest(double amount) {
        String userAuthorization = createUser();
        int id = createAccount(userAuthorization);
        double balance = getBalance(userAuthorization, id);
        depositMoney(userAuthorization, id, amount, HttpStatus.SC_BAD_REQUEST);

        assertEquals(balance, getBalance(userAuthorization, id));
    }

    @Test
    public void userCannotDepositMoneyToSomeoneOrToNonexistentAccountTest() {
        String userAuthorization = createUser();
        int id = createAccount(userAuthorization);

        int maxId = given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getList("id", Integer.class)
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        depositMoney(userAuthorization, ++id, 500, HttpStatus.SC_FORBIDDEN);
    }
}
