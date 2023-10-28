package solution.com.lattmat.service;

import solution.com.lattmat.model.Users;
import solution.com.lattmat.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<Users> findUserByPhoneNumber(String phoneNumber);
    Optional<Users> findUserByUsername(String username);
    Optional<Users> findUsersByOauthLoginId(String userid);
    UserDto saveUser(UserDto user);
}
