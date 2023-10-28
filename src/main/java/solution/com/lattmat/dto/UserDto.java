package solution.com.lattmat.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import solution.com.lattmat.security.domain.SecurityUser;
import solution.com.lattmat.security.enumeration.LoginProvider;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto{

    private UUID id;
    private String oauthLoginId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String mail;
    private String profileImage;
    private boolean isActive;
    private boolean isLock;

    private LoginProvider provider;

}
