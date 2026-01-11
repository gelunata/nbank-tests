package common.extensions;

import api.models.AccountResponse;
import api.models.CreateUserRequest;
import api.requests.steps.AccountsSteps;
import api.specs.RequestSpecs;
import common.annotations.Transfer;
import common.storage.SessionStorage;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TransferExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Transfer annotation = context.getRequiredTestMethod().getAnnotation(Transfer.class);
        if (annotation != null) {
            int authSender = annotation.authSender();
            int authRecipient = annotation.authRecipient();

            AccountResponse accountSender = SessionStorage.getSteps(authSender).getAllAccounts().getFirst();
            long accountRecipientId = authSender == authRecipient ?
                    SessionStorage.getSteps(authSender).getAllAccounts().get(1).getId() :
                    SessionStorage.getSteps(authRecipient).getAllAccounts().getFirst().getId();

            CreateUserRequest userSender = SessionStorage.getUser(authSender);
            String authHeaderSender = RequestSpecs.getUserAuthHeader(userSender.getUsername(), userSender.getPassword());

            AccountsSteps.transferMoney(authHeaderSender, accountSender.getId(), accountRecipientId, accountSender.getBalance());
        }
    }
}
