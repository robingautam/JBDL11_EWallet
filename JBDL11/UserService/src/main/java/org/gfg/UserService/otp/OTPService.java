package org.gfg.UserService.otp;

import org.springframework.stereotype.Component;

@Component
public class OTPService {


    private static final int MAX_OTP_LENGTH = 6;

    public String generateOtp(){
        StringBuilder otp = new StringBuilder();
        for (int i=1;i<=MAX_OTP_LENGTH;i++){
            int digit = (int)(Math.random()*10);
            otp.append(digit);
        }

        return otp.toString();
    }




}
