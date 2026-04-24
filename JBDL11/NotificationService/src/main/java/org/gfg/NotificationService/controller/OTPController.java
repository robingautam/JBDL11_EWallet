package org.gfg.NotificationService.controller;


import org.gfg.NotificationService.worker.OTPWorker;
import org.gfg.model.OTPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp-service")
public class OTPController {


    @Autowired
    OTPWorker otpWorker;

    @PostMapping("/send/otp")
    public String sendOTP(@RequestBody OTPRequest otpRequest){

        System.out.println("OTP Request Received");

        String otp = otpRequest.getOtp();
        String email = otpRequest.getEmail();

       return otpWorker.sendOTP(email,otp);

    }
}
