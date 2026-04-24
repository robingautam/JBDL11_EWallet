package org.gfg.NotificationService.consumer;

import org.gfg.NotificationService.worker.EmailWorker;
import org.gfg.constants.CommonConstants;
import org.gfg.model.UserIdentifier;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedConsumer {


    @Autowired
    EmailWorker emailWorker;

    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC, groupId = "user-group")
    public void consumerNewlyCreatedUser(String data){
        JSONObject jsonObject = new JSONObject(data);
        int userId = jsonObject.optInt(CommonConstants.USER_ID);
        String userName = jsonObject.optString(CommonConstants.USER_NAME);
        String userEmail = jsonObject.optString(CommonConstants.USER_EMAIL);

        UserIdentifier userIdentifier = jsonObject.optEnum(UserIdentifier.class,CommonConstants.USER_IDENTIFIER);

        String userIdentifierValue = jsonObject.optString(CommonConstants.USER_IDENTIFIER_VALUE);

        emailWorker.sendEmailWelcomeNotification(userName,userEmail,userIdentifier,userIdentifierValue);



    }
}
