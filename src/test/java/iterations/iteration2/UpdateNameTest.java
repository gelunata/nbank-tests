package iterations.iteration2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import requests.steps.AdminSteps;
import requests.steps.CustomerSteps;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateNameTest {
    @ValueSource(strings = {"John A", "a black"})
    @ParameterizedTest
    public void userCanUpdateNameTest(String newName) {
        String userAuthorization = AdminSteps.createUser();
        CustomerSteps.updateName(userAuthorization, newName);

        assertEquals(newName, CustomerSteps.getName(userAuthorization));
    }

    @ValueSource(strings = {"", "John", "Maria Anna Soul", // если не два слова
            "Poll 2", "1 Rich", "Rock Tomas3"// если не только буквы
    })
    @ParameterizedTest
    public void userCannotUpdateNameTest(String newName) {
        String userAuthorization = AdminSteps.createUser();
        String name = CustomerSteps.getName(userAuthorization);
        CustomerSteps.updateName(userAuthorization, newName);

        assertEquals(name, CustomerSteps.getName(userAuthorization));
    }
}
