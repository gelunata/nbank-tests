package iteration2;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class BaseTest {
    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter()));
    }

    protected String createUser() {
        String username = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String user = String.format("""
                {
                "username": "%s",
                "password": "Qwerty1!",
                "role": "USER"
                }
                """, username);

        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .body(user)
                .post("http://localhost:4111/api/v1/admin/users")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .header("Authorization");
    }

    public int createAccount(String userAuthorization) {
        return given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post("http://localhost:4111/api/v1/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .response()
                .jsonPath()
                .getInt("id");
    }

    public double getBalance(String userAuthorization, int accountId) {
        return given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getDouble(String.format("find { it.id == %d }.balance", accountId));
    }

    public void depositMoney(String userAuthorization, int accountId, double amount, int httpStatus) {
        given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format(Locale.US, """
                        {
                          "id": %d,
                          "balance": %.2f
                        }
                        """, accountId, amount))
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .assertThat()
                .statusCode(httpStatus);
    }
}
