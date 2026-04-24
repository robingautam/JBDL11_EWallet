package org.gfg.UserService.service;

import com.google.gson.Gson;
import org.gfg.UserService.model.User;
import org.gfg.UserService.otp.OTPService;
import org.gfg.UserService.repository.UserRepository;
import org.gfg.UserService.request.UserRequest;
import org.gfg.constants.CommonConstants;
import org.gfg.model.OTPRequest;
import org.gfg.model.UserResponse;
import org.gfg.model.UserStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OTPService otpService;

    @Autowired
    @Qualifier("otpRedisTemplate")
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public User createUser(UserRequest userRequest){

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setDob(userRequest.getDob());
        user.setPhoneNo(userRequest.getPhoneNo());
        user.setUserIdentifier(userRequest.getUserIdentifier());
        user.setUserIdentifierValue(userRequest.getUserIdentifierValue());
        user.setRole("NORMAL");
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        String userOtp = otpService.generateOtp();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        try {
            redisTemplate.opsForValue().set("OTP-"+userRequest.getEmail(),userOtp,150,TimeUnit.SECONDS);
            redisTemplate.opsForValue().set("USER-"+userRequest.getEmail(),userJson,150,TimeUnit.SECONDS);

            OTPRequest otpRequest = new OTPRequest();
            otpRequest.setOtp(userOtp);
            otpRequest.setEmail(userRequest.getEmail());
            HttpEntity<OTPRequest> entity = new HttpEntity<>(otpRequest);
            String response = restTemplate.postForObject("http://localhost:8081/otp-service/send/otp",entity, String.class);
            if (response.equals("OK")){
                User returnUser = new User();
                returnUser.setName(userRequest.getName());
                returnUser.setEmail(userRequest.getEmail());
                return returnUser;
            }else {
                return new User();
            }
        }
        catch (Exception e){
            System.out.println("Exception: "+e);
            return null;
        }
    }


    public User validateOTPAndCreateUser(String email, String otp){
        String redisOtp = redisTemplate.opsForValue().get("OTP-"+email);
        assert redisOtp != null;
        if (redisOtp.equals(otp)){
            System.out.println("OTP is correct");
            String jsonUser = redisTemplate.opsForValue().get("USER-"+email);
            try {
               Gson gson = new Gson();
               User user = gson.fromJson(jsonUser,User.class);
               User savedUser =  userRepository.save(user);
                // send data to kafka
                String kafkaData = constructKafkaUserData(savedUser);
                kafkaTemplate.send(CommonConstants.USER_CREATION_TOPIC,kafkaData);
                return savedUser;
            }
            catch (Exception e){
                System.out.println(e);
                return null;
            }
        }
        else {
            System.out.println("OTP is incorrect");
            return null;
        }

    }



    public String constructKafkaUserData(User user){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_ID, user.getId());
        jsonObject.put(CommonConstants.USER_NAME,user.getName());
        jsonObject.put(CommonConstants.USER_EMAIL,user.getEmail());
        jsonObject.put(CommonConstants.USER_MOBILE,user.getPhoneNo());
        jsonObject.put(CommonConstants.USER_IDENTIFIER,user.getUserIdentifier());
        jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE,user.getUserIdentifierValue());

        return jsonObject.toString();
    }



    public UserResponse getUserByUserName(String username){
       User user = userRepository.findByPhoneNo(username);
        System.out.println(user);
       UserResponse userResponse = new UserResponse();
       userResponse.setUsername(user.getPhoneNo());
       userResponse.setPassword(user.getPassword());

       return userResponse;
    }
}
