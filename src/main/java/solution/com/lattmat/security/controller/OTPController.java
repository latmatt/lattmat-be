package solution.com.lattmat.security.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solution.com.lattmat.constant.SecurityConstant;
import solution.com.lattmat.controller.BaseController;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.security.model.OTPRequest;
import solution.com.lattmat.security.model.OTPValidateRecord;
import solution.com.lattmat.security.service.OTPService;

@RestController
@AllArgsConstructor
@RequestMapping("/otp")
public class OTPController extends BaseController {

    private OTPService otpService;

    @PostMapping
    public ResponseEntity<CustomResponse> getOtp(@RequestParam(required = true) String phoneNumber, HttpSession session){
        String otp = otpService.generateOTP(phoneNumber);
        session.setAttribute(SecurityConstant.OTP, otp);
        return createResponse(true, HttpStatus.OK, otp, "OTP generated...");
    }

    @PostMapping("/validate")
    public ResponseEntity<CustomResponse> validateOTP(@RequestBody OTPValidateRecord validateRequest){
        boolean isValid = otpService.validateOTP(validateRequest.otp(), validateRequest.phoneNumber());
        return createResponse(isValid, HttpStatus.OK, null, "IS_VALID - " + isValid);
    }

}
