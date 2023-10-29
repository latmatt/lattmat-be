package solution.com.lattmat.security.service.impl;

import org.springframework.stereotype.Service;
import solution.com.lattmat.constant.SecurityConstant;
import solution.com.lattmat.security.service.OTPService;
import solution.com.lattmat.utils.SessionUtils;

@Service
public class OTPServiceImpl implements OTPService {

    @Override
    public String generateOTP(String phoneNumber) {
        return "123456";
    }

    @Override
    public boolean validateOTP(String otp, String phoneNumber) {
        String otpFromSession = (String) SessionUtils.getAttribute(SecurityConstant.OTP);
        return otp.equals(otpFromSession);
    }
}
