package org.gfg.WalletService.consumer;

import org.gfg.WalletService.model.Wallet;
import org.gfg.WalletService.service.WalletService;
import org.gfg.constants.CommonConstants;
import org.gfg.model.UserIdentifier;
import org.gfg.model.WalletStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    WalletService walletService;


    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC, groupId = "wallet-group")
    public void listenNewlyCreatedUser(String data){
        JSONObject jsonObject = new JSONObject(data);
        int userId = jsonObject.optInt(CommonConstants.USER_ID);
        String userMobile = jsonObject.optString(CommonConstants.USER_MOBILE);

        UserIdentifier userIdentifier = jsonObject.optEnum(UserIdentifier.class,CommonConstants.USER_IDENTIFIER);

        String userIdentifierValue = jsonObject.optString(CommonConstants.USER_IDENTIFIER_VALUE);


        Wallet wallet = Wallet.builder().userId(userId).userIdentifier(userIdentifier).userIdentifierValue(userIdentifierValue)
                .walletStatus(WalletStatus.ACTIVE)
                .phoneNo(userMobile)
                .build();

        walletService.createWalletAccount(wallet);
    }
}
