package solution.com.lattmat.security.service.impl;

import org.springframework.stereotype.Service;
import solution.com.lattmat.constant.SecurityConstant;
import solution.com.lattmat.dto.OtpDto;
import solution.com.lattmat.exception.domain.OTPExpiredException;
import solution.com.lattmat.security.service.OTPService;
import solution.com.lattmat.utils.SessionUtils;

import java.util.Date;

@Service
public class OTPServiceImpl implements OTPService {

    @Override
    public String generateOTP(String phoneNumber) {
        return phoneNumber.substring(0, 6);
    }

    @Override
    public boolean validateOTP(String otp, String phoneNumber) {
        boolean isOtpValid = false;
        OtpDto otpDto = (OtpDto) SessionUtils.getAttribute(SecurityConstant.OTP);

        if(new Date().getTime() > otpDto.getExpiredTime()) {
            throw new OTPExpiredException("Your otp is expired. Please request new one");
        }

        if(phoneNumber.equals(otpDto.getPhoneNumber())
                && otp.equals(otpDto.getOtp())){
            isOtpValid = true;
        }

        return isOtpValid;
    }
}
