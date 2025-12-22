package ui.iteration2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import generators.RandomData;
import models.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import requests.steps.AdminSteps;
import requests.steps.CustomerSteps;
import ui.SoftAssertionsTest;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class UpdateNameTest extends SoftAssertionsTest {
    @Test
    public void userCanUpdateNameTest() {
        String newName = RandomData.getName();
        CreateUserRequest userRequest = AdminSteps.createUserRequest();
        String userAuthHeader = AdminSteps.createUser(userRequest.getUsername(), userRequest.getPassword());

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $("div.profile-header").click();

        $(Selectors.byAttribute("placeholder", "Enter new name"))
                .shouldBe(Condition.visible)
                .setValue(newName);

        $(Selectors.byText("\uD83D\uDCBE Save Changes")).click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("✅ Name updated successfully!");
        alert.accept();

        $(byText("✏\uFE0F Edit Profile")).shouldBe(Condition.visible);
        softly.assertThat($("span.user-name").getText()).contains(newName);
        // !!! БАГ !!! Не изменяется, пока не обновишь страницу!!!

        softly.assertThat(CustomerSteps.getName(userAuthHeader)).isEqualTo(newName);
    }

    @Test
    public void userCannotUpdateNameTest() {
        String name = "Noname";
        String newName = RandomData.getUsername();
        CreateUserRequest userRequest = AdminSteps.createUserRequest();
        String userAuthHeader = AdminSteps.createUser(userRequest.getUsername(), userRequest.getPassword());

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);
        Selenide.open("/dashboard");

        $("div.profile-header").click();

        $(Selectors.byAttribute("placeholder", "Enter new name"))
                .shouldBe(Condition.visible)
                .setValue(newName)
                .shouldBe(Condition.visible)
                .setValue(newName);

        $(Selectors.byText("\uD83D\uDCBE Save Changes")).click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("Name must contain two words with letters only");
        alert.accept();

        $(byText("✏\uFE0F Edit Profile")).shouldBe(Condition.visible);
        softly.assertThat($("span.user-name").getText()).contains(name);

        softly.assertThat(CustomerSteps.getName(userAuthHeader)).isNull();
    }
}
