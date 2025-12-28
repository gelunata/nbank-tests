package ui.pages;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum BankAlert {
    USER_CREATED_SUCCESSFULLY("✅ User created successfully!"),
    USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS("Username must be between 3 and 15 characters"),
    NEW_ACCOUNT_CREATED("✅ New Account Created! Account Number: "),
    SUCCESSFULLY_DEPOSITED_TO_ACCOUNT("✅ Successfully deposited $%s to account %s!"),
    PLEASE_DEPOSIT_LESS_OR_EQUAL_TO_5000("❌ Please deposit less or equal to 5000$."),
    PLEASE_SELECT_AN_ACCOUNT("❌ Please select an account."),
    SUCCESSFULLY_TRANSFERRED_TO_ACCOUNT("✅ Successfully transferred $%.2f to account %s!"),
    ERROR_INVALID_TRANSFER("❌ Error: Invalid transfer: insufficient funds or invalid accounts"),
    PLEASE_FILL_ALL_FIELDS_AND_CONFIRM("❌ Please fill all fields and confirm."),
    TRANSFER_SUCCESSFUL_FROM_ACCOUNT_TO_ACCOUNT("✅ Transfer of $%s successful from Account %d to %d!"),
    TRANSFER_FAILED_PLEASE_TRY_AGAIN("❌ Transfer failed: Please try again."),
    NAME_UPDATE_SUCCESSFULLY("✅ Name updated successfully!"),
    NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY("Name must contain two words with letters only");

    private final String message;

    BankAlert(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(Locale.UK, message, args);
    }
}
