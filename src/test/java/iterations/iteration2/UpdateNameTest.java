package iterations.iteration2;

import methods.Name;
import methods.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateNameTest {
    @ValueSource(strings = {"John A", "a black"})
    @ParameterizedTest
    public void userCanUpdateNameTest(String newName) {
        String userAuthorization = User.create();
        Name.updateRequestReturnOK(userAuthorization, newName);
        assertEquals(newName, Name.get(userAuthorization));

    }

    @ValueSource(strings = {"", "John", "Maria Anna Soul", // если не два слова
            "Poll 2", "1 Rich", "Rock Tomas3"// если не только буквы
    })
    @ParameterizedTest
    public void userCannotUpdateNameTest(String newName) {
        String userAuthorization = User.create();
        String name = Name.get(userAuthorization);
        Name.updateRequestReturnBadRequest(userAuthorization, newName);
        assertEquals(name, Name.get(userAuthorization));
    }
}
