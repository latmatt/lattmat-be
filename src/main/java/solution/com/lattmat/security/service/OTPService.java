package solution.com.lattmat.security.service;

public interface OTPService {
    String generateOTP(String phoneNumber);
    boolean validateOTP(String otp, String phoneNumber);
}
