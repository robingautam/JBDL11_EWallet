package org.gfg.UserService.controller;

import org.gfg.UserService.model.User;
import org.gfg.UserService.request.UserRequest;
import org.gfg.UserService.response.UserCreationResponse;
import org.gfg.UserService.service.UserService;
import org.gfg.model.OTPRequest;
import org.gfg.model.OTPResponse;
import org.gfg.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service")
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping("/create/user")
    public ResponseEntity<UserCreationResponse> createUser(@RequestBody UserRequest userRequest){
        UserCreationResponse userCreationResponse = new UserCreationResponse();

        if (userRequest.getName()==null || userRequest.getEmail()==null || userRequest.getPassword()==null || userRequest.getPhoneNo()==null){
            userCreationResponse.setStatus("FAILED");
            userCreationResponse.setMessage("Provide mandatory information");
            return new ResponseEntity<>(userCreationResponse,HttpStatus.BAD_REQUEST);
        }

        User user = userService.createUser(userRequest);
        if (user==null){
            userCreationResponse.setStatus("FAILED");
            userCreationResponse.setMessage("Account Not Created");
            return new ResponseEntity<>(userCreationResponse,HttpStatus.OK);
        }

        if (user.getEmail()==null || user.getName()==null){
            userCreationResponse.setStatus("FAILED");
            userCreationResponse.setMessage("OTP Not sent");
            return new ResponseEntity<>(userCreationResponse,HttpStatus.OK);
        }

        userCreationResponse.setStatus("SUCCESS");
        userCreationResponse.setMessage("OTP has been sent successfully");
        userCreationResponse.setName(user.getName());
        userCreationResponse.setEmail(user.getEmail());

        return new ResponseEntity<>(userCreationResponse,HttpStatus.CREATED);
    }

    @PostMapping("/validate/otp")
    public ResponseEntity<OTPResponse> validateUserOTP(@RequestBody OTPRequest otpRequest){
        String otp = otpRequest.getOtp();
        String email = otpRequest.getEmail();
        OTPResponse otpResponse = new OTPResponse();
        if (otp.isEmpty() || email.isEmpty()){
            otpResponse.setMessage("provide correct details");
            otpResponse.setStatus("FAILED");
            return new ResponseEntity<>(otpResponse,HttpStatus.BAD_REQUEST);
        }

        User user = userService.validateOTPAndCreateUser(email, otp);
        if (user==null){
            otpResponse.setMessage("Incorrect OTP");
            otpResponse.setStatus("FAILED");
            return new ResponseEntity<>(otpResponse,HttpStatus.OK);
        }
        otpResponse.setMessage("Validated Successfully");
        otpResponse.setStatus("SUCCESS");
        return new ResponseEntity<>(otpResponse,HttpStatus.OK);
    }

    @GetMapping("/fetch/user/{username}")
    public UserResponse fetchUserInfoByUserName(@PathVariable("username") String username){
        System.out.println(username);
        return userService.getUserByUserName(username);
    }

}
