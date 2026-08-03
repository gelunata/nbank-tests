package api.iteration2;

import api.BaseTest;
import api.dao.UserDao;
import api.dao.comparison.DaoAndModelAssertions;
import api.models.CreateUserRequest;
import api.models.UserProfileResponse;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import api.requests.steps.DataBaseSteps;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateNameTest extends BaseTest {
    @ValueSource(strings = {"John A", "a black"})
    @ParameterizedTest
    public void userCanUpdateNameTest(String newName) {
        CreateUserRequest createUserRequest = AdminSteps.createUserRequest();
        String username = createUserRequest.getUsername();
        String userAuthorization = AdminSteps.createUser(username, createUserRequest.getPassword());
        UserProfileResponse userProfileResponse = CustomerSteps.updateName(userAuthorization, newName);

        softly.assertThat(CustomerSteps.getName(userAuthorization)).isEqualTo(newName);

        UserDao userDao = DataBaseSteps.getUserByUsername(username);
        DaoAndModelAssertions.assertThat(userProfileResponse, userDao).match();
    }

    @ValueSource(strings = {"", "John", "Maria Anna Soul", // если не два слова
            "Poll 2", "1 Rich", "Rock Tomas3"// если не только буквы
    })
    @ParameterizedTest
    public void userCannotUpdateNameTest(String newName) {
        CreateUserRequest createUserRequest = AdminSteps.createUserRequest();
        String username = createUserRequest.getUsername();
        String userAuthorization = AdminSteps.createUser(username, createUserRequest.getPassword());
        String name = CustomerSteps.getName(userAuthorization);
        CustomerSteps.updateNameFailed(userAuthorization, newName);

        assertEquals(name, CustomerSteps.getName(userAuthorization));

        UserDao userDao = DataBaseSteps.getUserByUsername(username);
        softly.assertThat(userDao.getName()).isEqualTo(name);
    }
}
