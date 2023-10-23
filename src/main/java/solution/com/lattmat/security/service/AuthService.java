package solution.com.lattmat.security.service;

import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.security.model.SignUpUserRecord;

public interface AuthService {
    UserDto signUp(SignUpUserRecord user);
    void login();
}
