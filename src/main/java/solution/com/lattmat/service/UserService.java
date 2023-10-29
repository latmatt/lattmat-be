package solution.com.lattmat.service;

import solution.com.lattmat.model.Users;
import solution.com.lattmat.dto.UserDto;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<Users> findUserByPhoneNumber(String phoneNumber);
    Optional<Users> findUserByUsername(String username);
    Optional<Users> findUsersByLoginId(String userid);
    Optional<Users> findUsersById(UUID id);
    UserDto saveUser(UserDto user);
}
