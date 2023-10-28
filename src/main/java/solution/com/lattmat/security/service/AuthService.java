package solution.com.lattmat.security.service;

import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.model.LoginUserRecord;
import solution.com.lattmat.security.model.SignUpUserRecord;

public interface AuthService {
    UserDto register(SignUpUserRecord user);
    Users login(LoginUserRecord user);
}
