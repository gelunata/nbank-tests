package ui.pages;

import java.util.Arrays;
import java.util.Locale;

/**
 * Перечисление, содержащее ожидаемые тексты уведомлений (алертов) банковской системы.
 */
@SuppressWarnings("checkstyle:JavadocVariable")
public enum BankAlert {
    USER_CREATED_SUCCESSFULLY,
    USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS,
    NEW_ACCOUNT_CREATED,
    SUCCESSFULLY_DEPOSITED_TO_ACCOUNT,
    PLEASE_DEPOSIT_LESS_OR_EQUAL_TO_5000,
    PLEASE_SELECT_AN_ACCOUNT,
    SUCCESSFULLY_TRANSFERRED_TO_ACCOUNT,
    ERROR_INVALID_TRANSFER,
    PLEASE_FILL_ALL_FIELDS_AND_CONFIRM,
    TRANSFER_SUCCESSFUL_FROM_ACCOUNT_TO_ACCOUNT,
    NAME_UPDATE_SUCCESSFULLY,
    NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY;

    /**
     * Возвращает текст сообщения, соответствующий константе.
     *
     * @return строка сообщения
     */
    public String getMessage() {
        switch (this) {
            case USER_CREATED_SUCCESSFULLY:
                return "✅ User created successfully!";
            case USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS:
                return "Username must be between 3 and 15 characters";
            case NEW_ACCOUNT_CREATED:
                return "✅ New Account Created! Account Number: ";
            case SUCCESSFULLY_DEPOSITED_TO_ACCOUNT:
                return "✅ Successfully deposited $%s to account %s!";
            case PLEASE_DEPOSIT_LESS_OR_EQUAL_TO_5000:
                return "❌ Please deposit less or equal to 5000$.";
            case PLEASE_SELECT_AN_ACCOUNT:
                return "❌ Please select an account.";
            case SUCCESSFULLY_TRANSFERRED_TO_ACCOUNT:
                return "✅ Successfully transferred $%s to account %s!";
            case ERROR_INVALID_TRANSFER:
                return "Invalid transfer: insufficient funds or invalid accounts";
            case PLEASE_FILL_ALL_FIELDS_AND_CONFIRM:
                return "❌ Please fill all fields and confirm.";
            case TRANSFER_SUCCESSFUL_FROM_ACCOUNT_TO_ACCOUNT:
                return "✅ Transfer of $%f successful from Account %d to %d!";
            case NAME_UPDATE_SUCCESSFULLY:
                return "✅ Name updated successfully!";
            case NAME_MUST_CONTAIN_TWO_WORDS_WITH_LETTERS_ONLY:
                return "Name must contain two words with letters only";
            default:
                throw new IllegalArgumentException("Unknown alert: " + this);
        }
    }

    /**
     * Форматирует строку уведомления с подстановкой переданных аргументов.
     *
     * @param args аргументы для форматирования строки
     * @return отформатированный текст уведомления
     */
    public String format(final Object... args) {
        final Object[] newArgs = Arrays.stream(args).map(arg -> {
            if (arg instanceof Double) {
                return String.format(Locale.UK, "%.2f", arg).replaceAll("\\.?0+$", "");
            } else {
                return arg;
            }
        }).toArray();

        return String.format(Locale.UK, this.getMessage(), newArgs);
    }
}
