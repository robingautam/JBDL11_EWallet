package org.gfg.TransactionService.service;

import org.gfg.TransactionService.model.Transaction;
import org.gfg.TransactionService.model.TransactionResponse;
import org.gfg.TransactionService.repository.TransactionRepository;
import org.gfg.TransactionService.request.TxnRequest;
import org.gfg.constants.CommonConstants;
import org.gfg.model.TxnStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public String initiateTransaction(TxnRequest txnRequest, String senderId){
        String receiverId = txnRequest.getReceiverId();
        double amount = txnRequest.getAmount();
        String purpose = txnRequest.getPurpose();

        String txnId = UUID.randomUUID().toString();

        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .amount(amount)
                .txnStatus(TxnStatus.INITIATED)
                .purpose(purpose)
                .txnStatusMessage("Transaction Initiated")
                .txnId(txnId)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        //send transaction information to kafka
        String txnData = getTransactionInformation(savedTransaction);
        kafkaTemplate.send(CommonConstants.TXN_CREATION_TOPIC,txnData);

        System.out.println("txn data send to Kafka");

        return txnId;
    }

    public String getTransactionInformation(Transaction transaction){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.TXN_ID,transaction.getTxnId());
        jsonObject.put(CommonConstants.TXN_AMOUNT, transaction.getAmount());
        jsonObject.put(CommonConstants.SENDER_ID,transaction.getSenderId());
        jsonObject.put(CommonConstants.RECEIVER_ID, transaction.getReceiverId());

        return jsonObject.toString();
    }


    public List<TransactionResponse> getTransactionHistory(String mobileNo){
      List<Transaction> transactionList =  transactionRepository.findBySenderIdOrReceiverId(mobileNo,mobileNo);
      List<TransactionResponse> transactionResponseList = new ArrayList<>();
      for (Transaction t: transactionList){
          TransactionResponse transactionResponse = new TransactionResponse();
          transactionResponse.setTxnId(t.getTxnId());
          transactionResponse.setTransactionTime(t.getUpdatedOn());;
          transactionResponse.setAmount(t.getAmount());
          transactionResponse.setTxnStatus(t.getTxnStatus());
          if (mobileNo.equals(t.getSenderId())){
              transactionResponse.setTxnType("DEBIT");
              transactionResponse.setThirdParty(t.getReceiverId());
          }else {
              transactionResponse.setTxnType("CREDIT");
              transactionResponse.setThirdParty(t.getReceiverId());
          }
          transactionResponseList.add(transactionResponse);
      }

      return transactionResponseList;
    }
}
