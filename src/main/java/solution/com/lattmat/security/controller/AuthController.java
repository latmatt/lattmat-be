package solution.com.lattmat.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import solution.com.lattmat.security.model.SignUpUserRecord;
import solution.com.lattmat.security.service.AuthService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping
    public void signUp(@RequestBody SignUpUserRecord user){
        authService.signUp(user);
    }

}
