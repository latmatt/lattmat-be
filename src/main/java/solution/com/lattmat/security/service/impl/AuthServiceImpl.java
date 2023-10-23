package solution.com.lattmat.security.service.impl;

import lombok.AllArgsConstructor;
import org.aspectj.bridge.Message;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import solution.com.lattmat.constant.MessageConstant;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.exception.domain.PhoneNumberAlreadyExistException;
import solution.com.lattmat.exception.domain.UsernameAlreadyExistException;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.model.SignUpUserRecord;
import solution.com.lattmat.security.service.AuthService;
import solution.com.lattmat.service.UserService;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private ModelMapper modelMapper;
    private UserService userService;

    @Override
    public UserDto signUp(SignUpUserRecord user) {

        Optional<Users> usersOptional = userService.findUserByPhoneNumber(user.phoneNumber());
        if(usersOptional.isPresent()){
            throw new PhoneNumberAlreadyExistException(MessageConstant.USERNAME_ALREADY_EXIST);
        }

        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userService.saveUser(userDto);
    }

    @Override
    public void login() {

    }
}
