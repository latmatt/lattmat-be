package solution.com.lattmat.security.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solution.com.lattmat.constant.SecurityConstant;
import solution.com.lattmat.security.model.OTPRequest;
import solution.com.lattmat.security.model.OTPValidateRecord;
import solution.com.lattmat.security.service.OTPService;

@RestController
@AllArgsConstructor
@RequestMapping("/otp")
public class OTPController {

    private OTPService otpService;

    @PostMapping
    public ResponseEntity<String> getOtp(@RequestBody OTPRequest otpRequest, HttpSession session){
        String otp = otpService.generateOTP(otpRequest.phoneNumber());
        session.setAttribute(SecurityConstant.OTP, otp);
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateOTP(@RequestBody OTPValidateRecord validateRequest){
        boolean isValid = otpService.validateOTP(validateRequest.otp(), validateRequest.phoneNumber());
        return ResponseEntity.ok(isValid);
    }

}
