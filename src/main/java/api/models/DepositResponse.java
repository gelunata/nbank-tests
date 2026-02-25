package api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositResponse extends BaseModel {
    private long id;
    private String accountNumber;
    private double balance;
    private double depositAmount;
    private long transactionId;
}
