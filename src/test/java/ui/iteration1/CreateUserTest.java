package ui.iteration1;

import api.models.CreateUserRequest;
import api.models.CreateUserResponse;
import api.models.comparision.ModelAssertions;
import api.requests.steps.AdminSteps;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.AdminPanel;
import ui.pages.BankAlert;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateUserTest extends BaseUiTest {
    @Test
    public void adminCanCreateUserTest() {
        CreateUserRequest admin = CreateUserRequest.builder()
                .username("admin")
                .password("admin")
                .build();

        authAsUser(admin);

        CreateUserRequest newUser = AdminSteps.createUserRequest();
        new AdminPanel().open().createUser(newUser.getUsername(), newUser.getPassword())
                .checkAlertMessageAndAccept(BankAlert.USER_CREATED_SUCCESSFULLY.getMessage())
                .getAllUsers()
                .findBy(Condition.exactText(newUser.getUsername() + "\nUSER"))
                .shouldBe(Condition.visible);

        CreateUserResponse createdUser = AdminSteps.getAllUsers().stream()
                .filter(user -> user.getUsername().equals(newUser.getUsername()))
                .findFirst()
                .get();

        ModelAssertions.assertThatModels(newUser, createdUser).match();
    }

    @Test
    public void adminCannotCreateUserWithInvalidDataTest() {
        CreateUserRequest admin = CreateUserRequest.builder()
                .username("admin")
                .password("admin")
                .build();

        authAsUser(admin);

        CreateUserRequest newUser = AdminSteps.createUserRequest("a");

        new AdminPanel().open().createUser(newUser.getUsername(), newUser.getPassword())
                .checkAlertMessageAndAccept(BankAlert.USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS.getMessage())
                .getAllUsers()
                .findBy(Condition.exactText(newUser.getUsername() + "\nUSER")).shouldNotBe(Condition.exist);

        long usersWithSameUsernameAsNewUser = AdminSteps.getAllUsers().stream()
                .filter(user -> user.getUsername().equals(newUser.getUsername()))
                .count();

        assertThat(usersWithSameUsernameAsNewUser).isZero();
    }
}
