package api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsResponse extends BaseModel {
    private long id;
    private double amount;
    private String type;
    private String timestamp;
    private String relatedAccount;
    private String timestampAsString;
    private long relatedAccountId;
    private double amountAsDouble;
}
