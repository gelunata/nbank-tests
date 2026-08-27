package api.models;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositRequest extends BaseModel {
    private Long accountId;
    private double amount;
    private String description;
}
