package org.gfg.WalletService.service;

import org.gfg.WalletService.model.Wallet;
import org.gfg.WalletService.repository.WalletRepository;
import org.gfg.constants.CommonConstants;
import org.gfg.model.TxnStatus;
import org.gfg.model.WalletStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Value("${wallet.initial.amount}")
    private String walletBalance;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public void createWalletAccount(Wallet wallet){

        wallet.setBalance(Double.parseDouble(walletBalance));

        walletRepository.save(wallet);

        System.out.println("Wallet account created successfully");
    }


    public void updateWalletAndSendTransaction(String senderId, String receiverId,double amount,String txnId){
        Wallet senderWallet = walletRepository.findByPhoneNo(senderId);
        Wallet receiverWallet = walletRepository.findByPhoneNo(receiverId);

        TxnStatus txnStatus;
        String txnStatusMessage;

        if (senderWallet==null || !WalletStatus.ACTIVE.equals(senderWallet.getWalletStatus())){
            txnStatus = TxnStatus.FAILED;
            txnStatusMessage = "sender wallet does not exist";
        }else if (receiverWallet==null || !WalletStatus.ACTIVE.equals(receiverWallet.getWalletStatus())){
            txnStatus = TxnStatus.FAILED;
            txnStatusMessage = "receiver wallet does not exist";
        }else if (senderWallet.getBalance()<amount){
            txnStatus = TxnStatus.FAILED;
            txnStatusMessage = "Insufficient Balance";
        }else {
            if (updateWalletBalance(senderId,receiverId,amount)){
                txnStatus = TxnStatus.SUCCESS;
                txnStatusMessage = "Transaction is successfull";
            }else {
                txnStatus = TxnStatus.PENDING;
                txnStatusMessage = "Transaction is pending";
            }
        }

        // send updated transaction to kafka
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.TXN_ID,txnId);
        jsonObject.put(CommonConstants.TXN_STATUS, txnStatus);
        jsonObject.put(CommonConstants.TXN_STATUS_MESSAGE, txnStatusMessage);

        kafkaTemplate.send(CommonConstants.TXN_UPDATE_TOPIC,jsonObject.toString());

        System.out.println("Updated txn details send to kafka");

    }

    @Transactional
    public boolean updateWalletBalance(String sender, String receiverId, double amount){
        try {
            walletRepository.updateWalletBalance(sender,-amount);
            walletRepository.updateWalletBalance(receiverId,amount);
           return true;
        }catch (Exception e){
            return false;
        }
    }


}
