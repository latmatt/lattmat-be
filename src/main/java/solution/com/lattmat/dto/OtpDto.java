package solution.com.lattmat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpDto {
    private String phoneNumber;
    private String otp;
    private long expiredTime;
}
