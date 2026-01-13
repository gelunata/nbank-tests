package ui.iteration2;

import api.generators.RandomData;
import api.requests.steps.CustomerSteps;
import api.specs.RequestSpecs;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.BankAlert;
import ui.pages.EditProfilePage;
import ui.pages.UserDashboard;

public class UpdateNameTest extends BaseUiTest {
    @Test
    @UserSession
    public void userCanUpdateNameTest() {
        String newName = RandomData.getName();

        String name = new UserDashboard().open()
                .editProfile()
                .getPage(EditProfilePage.class)
                .newName(newName)
                .checkAlertMessageAndAccept(BankAlert.NAME_UPDATE_SUCCESSFULLY.getMessage())
                .getName().getText();

        softly.assertThat(name).contains(newName);
        // !!! БАГ !!! Не изменяется, пока не обновишь страницу!!!

        String userAuthHeader = RequestSpecs.getUserAuthHeader(SessionStorage.getUser().getUsername(), SessionStorage.getUser().getPassword());
        softly.assertThat(CustomerSteps.getName(userAuthHeader)).isEqualTo(newName);
    }

    @Test
    @UserSession
    public void userCannotUpdateNameTest() {
        String name = "Noname";
        String newName = RandomData.getUsername();

        String actualName = new EditProfilePage().open()
                .newName(newName)
                .checkAlertMessageAndAccept(BankAlert.NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY.getMessage())
                .getName().getText();

        softly.assertThat(actualName).contains(name);

        String userAuthHeader = RequestSpecs.getUserAuthHeader(SessionStorage.getUser().getUsername(), SessionStorage.getUser().getPassword());
        softly.assertThat(CustomerSteps.getName(userAuthHeader)).isNull();
    }
}
