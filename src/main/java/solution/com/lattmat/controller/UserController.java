package solution.com.lattmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import solution.com.lattmat.convertor.RoleConverter;
import solution.com.lattmat.convertor.UserConverter;
import solution.com.lattmat.entity.Role;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.enumeration.UserRoleType;
import solution.com.lattmat.service.RoleService;
import solution.com.lattmat.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

//    @PostMapping("/convert")
//    public ResponseEntity<String> convert(@RequestParam UUID userId){
//        final Optional<Users> usersOptional = userService.findUsersById(userId);
//
//        Users user = usersOptional.get();
//        System.out.println(user.getRoles());
//
//        Role role = RoleConverter.dtoToEntity(roleService.getRoleByRoleType(UserRoleType.PREMIUM_USER));
//        user.addRole(role);
//
//        userService.saveUser(UserConverter.entityToDto(user));
//
//        return ResponseEntity.ok("SUCCESS");
//    }
}
