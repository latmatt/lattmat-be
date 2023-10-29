package solution.com.lattmat.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.repo.UserRepository;
import solution.com.lattmat.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Optional<Users> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findUsersByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Users> findUserByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    @Override
    public Optional<Users> findUsersByLoginId(String userid) {
        return userRepository.findUsersByLoginId(userid);
    }

    @Override
    public Optional<Users> findUsersById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDto saveUser(UserDto user) {
        Users u = modelMapper.map(user, Users.class);
        u = userRepository.save(u);
        return modelMapper.map(u, UserDto.class);
    }
}
