package iteration2;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateNameTest extends BaseTest {
    @ValueSource(strings = {"John A", "a black"})
    @ParameterizedTest
    public void userCanUpdateNameTest(String newName) {
        String userAuthorization = createUser();
        updateNameUser(userAuthorization, newName, HttpStatus.SC_OK);
        assertEquals(newName, getName(userAuthorization));

    }

    @ValueSource(strings = {"", "John", "Maria Anna Soul", // если не два слова
            "Poll 2", "1 Rich", "Rock Tomas3"// если не только буквы
    })
    @ParameterizedTest
    public void userCannotUpdateNameTest(String newName) {
        String userAuthorization = createUser();
        String name = getName(userAuthorization);
        updateNameUser(userAuthorization, newName, HttpStatus.SC_BAD_REQUEST);
        assertEquals(name, getName(userAuthorization));
    }

    private void updateNameUser(String userAuthorization, String name, int httpStatus) {
        given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format("""
                        {
                            "name": "%s"
                        }
                        """, name))
                .put("http://localhost:4111/api/v1/customer/profile")
                .then()
                .assertThat()
                .statusCode(httpStatus);
    }

    private String getName(String userAuthorization) {
        return given()
                .header("Authorization", userAuthorization)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get("http://localhost:4111/api/v1/customer/profile")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .get("name");
    }
}
