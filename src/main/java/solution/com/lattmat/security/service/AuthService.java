package solution.com.lattmat.security.service;

import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.security.model.LoginUserRecord;
import solution.com.lattmat.security.model.SignUpUserRecord;

import java.util.UUID;

public interface AuthService {
    UserDto register(SignUpUserRecord user);
    Users login(LoginUserRecord user);
    void changePassword(String oldPassword, String newPassword, UUID userId);
}
