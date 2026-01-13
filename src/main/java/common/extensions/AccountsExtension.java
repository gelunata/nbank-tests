package common.extensions;

import api.models.CreateUserRequest;
import api.requests.steps.AccountsSteps;
import api.requests.steps.UserSteps;
import api.specs.RequestSpecs;
import common.annotations.Accounts;
import common.storage.SessionStorage;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Map;

public class AccountsExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Accounts annotation = context.getRequiredTestMethod().getAnnotation(Accounts.class);
        if (annotation != null) {
            int accountCount = annotation.value();
            for (Map.Entry<CreateUserRequest, UserSteps> entry : SessionStorage.getUsers().entrySet()) {
                CreateUserRequest user = entry.getKey();
                String authHeader = RequestSpecs.getUserAuthHeader(user.getUsername(), user.getPassword());
                for (int i = 0; i < accountCount; i++) {
                    AccountsSteps.createAccount(authHeader);
                }
            }
        }
    }
}
