package common.extensions;

import api.generators.RandomData;
import api.models.CreateUserRequest;
import api.requests.steps.AccountsSteps;
import api.specs.RequestSpecs;
import common.annotations.Deposit;
import common.storage.SessionStorage;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DepositExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Deposit annotation = context.getRequiredTestMethod().getAnnotation(Deposit.class);
        if (annotation != null) {
            int auth = annotation.auth();

            long accountId = SessionStorage.getSteps(auth).getAllAccounts().getFirst().getId();

            CreateUserRequest user = SessionStorage.getUser(auth);
            String authHeader = RequestSpecs.getUserAuthHeader(user.getUsername(), user.getPassword());

            AccountsSteps.depositMoney(authHeader, accountId, RandomData.getDepositAmount());
        }
    }
}
