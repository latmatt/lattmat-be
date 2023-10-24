package solution.com.lattmat.security.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solution.com.lattmat.constant.MessageConstant;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.exception.domain.InvalidCredentialsException;
import solution.com.lattmat.exception.domain.PhoneNumberAlreadyExistException;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.model.LoginUserRecord;
import solution.com.lattmat.security.model.SignUpUserRecord;
import solution.com.lattmat.security.service.AuthService;
import solution.com.lattmat.security.utils.JwtUtilities;
import solution.com.lattmat.service.UserService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto signUp(SignUpUserRecord user) {

        Optional<Users> usersOptional = userService.findUserByPhoneNumber(user.phoneNumber());
        if(usersOptional.isPresent()){
            throw new PhoneNumberAlreadyExistException(MessageConstant.USERNAME_ALREADY_EXIST);
        }

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.saveUser(userDto);
    }

    @Override
    public Users login(LoginUserRecord user) {

        Users loginUser = userService.findUserByPhoneNumber(user.phoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if(passwordEncoder.matches(user.password(), loginUser.getPassword())){
            return loginUser;
        }

        throw new InvalidCredentialsException("Invalid credentials");

    }
}
