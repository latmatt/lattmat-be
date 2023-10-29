package solution.com.lattmat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import solution.com.lattmat.security.enumeration.LoginProvider;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto{

    private UUID id;
    private String loginId;
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
