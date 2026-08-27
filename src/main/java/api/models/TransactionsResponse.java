package api.models;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsResponse extends BaseModel {
    private long id;
    private double amount;
    private String type;
    private String timestamp;
    private long relatedAccountId;
}
