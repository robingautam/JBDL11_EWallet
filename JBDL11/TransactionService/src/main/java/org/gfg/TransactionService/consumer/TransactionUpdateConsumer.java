package org.gfg.TransactionService.consumer;

import org.gfg.TransactionService.repository.TransactionRepository;
import org.gfg.constants.CommonConstants;
import org.gfg.model.TxnStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionUpdateConsumer {


    @Autowired
    TransactionRepository transactionRepository;


    @KafkaListener(topics = CommonConstants.TXN_UPDATE_TOPIC, groupId = "txn-update-group")
    public void consumeUpdatedTransaction(String data){
        System.out.println("Updated Transaction Details: "+data);

        JSONObject jsonObject = new JSONObject(data);

        String txnId = jsonObject.optString(CommonConstants.TXN_ID);
        String txnStatusMessage = jsonObject.optString(CommonConstants.TXN_STATUS_MESSAGE);
        TxnStatus txnStatus = jsonObject.optEnum(TxnStatus.class,CommonConstants.TXN_STATUS);

        transactionRepository.updateTransactionDetails(txnId,txnStatus,txnStatusMessage);

        System.out.println("Transaction details updated");

    }
}
