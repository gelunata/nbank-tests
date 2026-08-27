package api.models;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest extends BaseModel {
    private long senderAccountId;
    private long receiverAccountId;
    private double amount;
    private String description;
}
