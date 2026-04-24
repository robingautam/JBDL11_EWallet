package org.gfg.TransactionService.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxnRequest {

    private String receiverId;
    double amount;
    private String purpose;
}
