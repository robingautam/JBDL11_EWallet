package org.gfg.TransactionService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gfg.model.TxnStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {

   private String txnId;
   private String txnType;
   private String thirdParty;
   private double amount;
   private TxnStatus txnStatus;
   private Date transactionTime;

}
