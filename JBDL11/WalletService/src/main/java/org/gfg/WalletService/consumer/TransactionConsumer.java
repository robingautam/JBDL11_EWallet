package org.gfg.WalletService.consumer;

import org.gfg.WalletService.service.WalletService;
import org.gfg.constants.CommonConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @Autowired
    WalletService walletService;

    @KafkaListener(topics = CommonConstants.TXN_CREATION_TOPIC, groupId = "txn-group")
    public void listenNewTransactions(String data){
        System.out.println("Transaction data receiced: "+data);

        JSONObject jsonObject = new JSONObject(data);

        String txnId = jsonObject.optString(CommonConstants.TXN_ID);
        String senderId = jsonObject.optString(CommonConstants.SENDER_ID);
        String receiverId = jsonObject.optString(CommonConstants.RECEIVER_ID);
        double amount = jsonObject.optDouble(CommonConstants.TXN_AMOUNT);

        walletService.updateWalletAndSendTransaction(senderId,receiverId,amount,txnId);
    }
}
