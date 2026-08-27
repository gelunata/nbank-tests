package common.extensions;

import api.generators.RandomData;
import api.models.CreateUserRequest;
import api.requests.steps.AccountSteps;
import api.specs.RequestSpecs;
import common.annotations.Deposit;
import common.storage.SessionStorage;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class DepositExtension implements BeforeEachCallback {
    /** Лимит разового пополнения счета. */
    private static final double DEPOSIT_LIMIT = 5000.0;

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        Deposit annotation = context.getRequiredTestMethod().getAnnotation(Deposit.class);
        if (annotation != null) {
            int auth = annotation.auth();

            long accountId = SessionStorage.getSteps(auth).getAllAccounts().getFirst().getId();

            CreateUserRequest user = SessionStorage.getUser(auth);
            String authHeader = RequestSpecs.getUserAuthHeader(user.getUsername(), user.getPassword());

            if (annotation.value() < 0) {
                AccountSteps.depositMoney(authHeader, accountId, RandomData.getDepositAmount());
            } else {
                double amount = annotation.value();
                do {
                    AccountSteps.depositMoney(authHeader, accountId, DEPOSIT_LIMIT);
                    amount -= DEPOSIT_LIMIT;
                } while (amount > 0);
            }
        }
    }
}
