package solution.com.lattmat.security.service.impl;

import org.springframework.stereotype.Service;
import solution.com.lattmat.constant.SecurityConstant;
import solution.com.lattmat.security.service.OTPService;
import solution.com.lattmat.utils.SessionUtils;

@Service
public class OTPServiceImpl implements OTPService {

    @Override
    public String generateOTP(String phoneNumber) {
        int length = phoneNumber.length();
        return phoneNumber.substring(length-6, length);
    }

    @Override
    public boolean validateOTP(String otp, String phoneNumber) {
        String otpFromSession = (String) SessionUtils.getAttribute(SecurityConstant.OTP);
        return otp.equals(otpFromSession);
    }
}
