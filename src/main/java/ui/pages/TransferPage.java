package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.util.Locale;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class TransferPage extends BasePage<TransferPage> {
    private SelenideElement senderAccountSelector = $("select.account-selector");
    private SelenideElement recipientNameInput = $(Selectors.byAttribute("placeholder", "Enter recipient name"));
    private SelenideElement recipientAccountInput = $(Selectors.byAttribute("placeholder", "Enter recipient account number"));
    private SelenideElement amountInput = $(Selectors.byAttribute("placeholder", "Enter amount"));
    private SelenideElement confirmCheck = $("#confirmCheck");
    private SelenideElement transferButton = $(byText("\uD83D\uDE80 Send Transfer"));
    private SelenideElement transferAgainButton = $(byText("🔁 Transfer Again"));
    private SelenideElement repeatButton = $("ul.list-group").find(byText("TRANSFER_IN")).parent()
            .find(withText("🔁 Repeat"));
    private SelenideElement repeatTransferTitle = $(byText("\uD83D\uDD01 Repeat Transfer"));
    private SelenideElement senderAccountRepeatSelector = $(Selectors.byText("-- Choose an account --")).parent();
    private SelenideElement amountRepeatInput = $("input.form-control[type='number']");
    private SelenideElement searchInput = $(Selectors.byAttribute("placeholder", "Enter name to find transactions"));
    private SelenideElement searchButton = $(byText("\uD83D\uDD0D Search Transactions"));
    private ElementsCollection matchingTransactions = $("ul.list-group").findAll("li");

    @Override
    public String url() {
        return "/transfer";
    }

    public TransferPage transfer(String senderAccount, String name, String recipientAccount, double amount, boolean confirm) {
        amountInput.sendKeys(String.valueOf(amount));
        return transfer(senderAccount, name, recipientAccount, confirm);
    }

    public TransferPage transfer(String senderAccount, String name, String recipientAccount, boolean confirm) {
        senderAccountSelector.selectOptionContainingText(senderAccount);
        recipientNameInput.sendKeys(name);
        recipientAccountInput.sendKeys(recipientAccount);
        confirmCheck.setSelected(confirm);
        transferButton.click();
        return this;
    }

    public TransferPage transferAgain() {
        transferAgainButton.click();
        return this;
    }

    public TransferPage repeat() {
        repeatButton.click();
        return this;
    }

    public TransferPage repeatTransfer(String senderAccount, double amount, boolean confirm) {
        fillValuesForRepeatTransfer(senderAccount, amount, confirm);
        transferButton.click();
        return this;
    }

    public TransferPage fillValuesForRepeatTransfer(String senderAccount, boolean confirm) {
        fillValuesForRepeatTransfer(senderAccount, 0.0, confirm);
        amountRepeatInput.setValue("");
        return this;
    }

    private void fillValuesForRepeatTransfer(String senderAccount, double amount, boolean confirm) {
        senderAccountRepeatSelector.selectOptionContainingText(senderAccount);
        amountRepeatInput.setValue(String.format(Locale.UK, "%.2f", amount));
        confirmCheck.setSelected(confirm);
    }

    public TransferPage searchTransactionByUsernameOrName(String filter) {
        searchInput.setValue(filter);
        searchButton.click();
        return this;
    }
}
