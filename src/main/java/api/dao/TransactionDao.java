package api.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDao {
    private long accountId;
    private double amount;
    private String type;
    private String relatedAccount;
    private long relatedAccountId;
}
