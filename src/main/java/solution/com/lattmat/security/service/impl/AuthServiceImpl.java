package solution.com.lattmat.security.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solution.com.lattmat.constant.MessageConstant;
import solution.com.lattmat.convertor.UserConverter;
import solution.com.lattmat.dto.RoleDto;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.enumeration.UserRoleType;
import solution.com.lattmat.exception.domain.InvalidCredentialsException;
import solution.com.lattmat.exception.domain.PhoneNumberAlreadyExistException;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.exception.domain.UserRoleNotFoundException;
import solution.com.lattmat.security.model.LoginUserRecord;
import solution.com.lattmat.security.model.SignUpUserRecord;
import solution.com.lattmat.security.service.AuthService;
import solution.com.lattmat.service.RoleService;
import solution.com.lattmat.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(SignUpUserRecord user) {

        UserDto userDto = null;

        try {
            Optional<Users> usersOptional = userService.findUsersByLoginId(user.phoneNumber());
            if(usersOptional.isPresent()){
                throw new PhoneNumberAlreadyExistException(MessageConstant.USERNAME_ALREADY_EXIST);
            }

            userDto = modelMapper.map(user, UserDto.class);
            userDto.setLoginId(user.phoneNumber());
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            RoleDto role = roleService.getRoleByRoleType(UserRoleType.ROLE_NORMAL_USER);
            userDto.setRoles(Set.of(role));
        } catch (UserRoleNotFoundException e) {
            throw e;
        }

        return userService.saveUser(userDto);
    }

    @Override
    public Users login(LoginUserRecord user) {

        Users loginUser = userService.findUsersByLoginId(user.phoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if(passwordEncoder.matches(user.password(), loginUser.getPassword())){
            return loginUser;
        }

        throw new InvalidCredentialsException("Invalid credentials");

    }

    @Override
    public void changePassword(String newPassword, Users user) {

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(UserConverter.entityToDto(user));

    }
}
