package common.extensions;

import api.models.CreateUserRequest;
import api.requests.steps.AdminSteps;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ui.pages.BasePage;

import java.util.LinkedList;
import java.util.List;

public final class UserSessionExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        UserSession annotation = context.getRequiredTestMethod().getAnnotation(UserSession.class);
        if (annotation != null) {
            int userCount = annotation.value();

            SessionStorage.clear();

            List<CreateUserRequest> users = new LinkedList<>();

            for (int i = 0; i < userCount; i++) {
                CreateUserRequest userRequest = AdminSteps.createUserRequest();
                AdminSteps.createUser(userRequest);

                users.add(userRequest);
            }

            SessionStorage.addUsers(users);

            int authAsUser = annotation.auth();
            if (authAsUser > 0) {
                BasePage.authAsUser(SessionStorage.getUser(authAsUser));
            }
        }
    }
}
