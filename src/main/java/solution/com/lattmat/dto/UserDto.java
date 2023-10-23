package solution.com.lattmat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto{
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String mail;
    private String profileImage;
    private boolean isActive;
    private boolean isLock;
}
