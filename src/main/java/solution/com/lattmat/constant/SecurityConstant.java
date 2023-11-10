package solution.com.lattmat.constant;

public class SecurityConstant {

    public static String OTP = "OTP";
    public static long OTP_EXPIRATION_IN_MILLIS = 60 * 1000;

    public static final String [] PUBLIC_URLS = {
            "/auth/**", "/otp/**", "/o/**", "/swagger-ui/**", "/v3/api-docs/**", "/user/**", "/oauth2/**", "/test/**", "/actuator/**"
    };

    public static final String [] PREMIUM_URLS = {
            "/premium/**"
    };
}
