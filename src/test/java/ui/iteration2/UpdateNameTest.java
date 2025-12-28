package ui.iteration2;

import api.generators.RandomData;
import api.models.CreateUserRequest;
import api.requests.steps.AdminSteps;
import api.requests.steps.CustomerSteps;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.EditProfilePage;
import ui.pages.UserDashboard;

public class UpdateNameTest extends BaseUiTest {
    @Test
    public void userCanUpdateNameTest() {
        String newName = RandomData.getName();
        CreateUserRequest userRequest = AdminSteps.createUserRequest();
        String userAuthHeader = AdminSteps.createUser(userRequest.getUsername(), userRequest.getPassword());

        authAsUser(userAuthHeader);

        String name = new UserDashboard().open()
                .editProfile()
                .getPage(EditProfilePage.class)
                .newName(newName)
                .checkAlertMessageAndAccept(BankAlert.NAME_UPDATE_SUCCESSFULLY.getMessage())
                .getName().getText();

        softly.assertThat(name).contains(newName);
        // !!! БАГ !!! Не изменяется, пока не обновишь страницу!!!

        softly.assertThat(CustomerSteps.getName(userAuthHeader)).isEqualTo(newName);
    }

    @Test
    public void userCannotUpdateNameTest() {
        String name = "Noname";
        String newName = RandomData.getUsername();
        CreateUserRequest userRequest = AdminSteps.createUserRequest();
        String userAuthHeader = AdminSteps.createUser(userRequest.getUsername(), userRequest.getPassword());

        authAsUser(userAuthHeader);
        String actualName = new EditProfilePage().open()
                .newName(newName)
                .checkAlertMessageAndAccept(BankAlert.NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY.getMessage())
                .getName().getText();

        softly.assertThat(actualName).contains(name);

        softly.assertThat(CustomerSteps.getName(userAuthHeader)).isNull();
    }
}
