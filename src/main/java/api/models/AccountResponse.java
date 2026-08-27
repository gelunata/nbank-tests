package api.models;

import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse extends BaseModel {
    private long id;
    private String accountNumber;
    private double balance;
    private List<TransactionsResponse> transactions;
}
