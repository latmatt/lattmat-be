package solution.com.lattmat.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import solution.com.lattmat.controller.BaseController;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.domain.MessageResponse;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.exception.domain.TokenRefreshException;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.config.SecurityConfigConst;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.model.LoginUserRecord;
import solution.com.lattmat.security.model.SignUpUserRecord;
import solution.com.lattmat.security.model.UserInfoResponse;
import solution.com.lattmat.security.service.AuthService;
import solution.com.lattmat.security.service.RefreshTokenService;
import solution.com.lattmat.security.utils.JwtUtilities;
import solution.com.lattmat.service.UserService;

import static solution.com.lattmat.security.config.SecurityConfigConst.REFRESH_TOKEN;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtilities jwtUtilities;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse> register(@RequestBody SignUpUserRecord user){

        UserDto newUser = authService.register(user);

        return createResponse(
                true, HttpStatus.CREATED,
                null,
                new UserInfoResponse(
                        newUser.getId(),
                        newUser.getUsername(), newUser.getFirstName(),
                        newUser.getLastName(), newUser.getPhoneNumber(),
                        newUser.getMail(), newUser.getProfileImage()),
                "Successfully registered");

    }

    @GetMapping("/oauth-register")
    public ResponseEntity<CustomResponse> oauthRegister(@RequestBody SignUpUserRecord user){

        UserDto newUser = authService.register(user);

        return createResponse(
                true, HttpStatus.CREATED,
                null,
                new UserInfoResponse(
                        newUser.getId(),
                        newUser.getUsername(), newUser.getFirstName(),
                        newUser.getLastName(), newUser.getPhoneNumber(),
                        newUser.getMail(), newUser.getProfileImage()),
                "Successfully registered");

    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse> login(@RequestParam(required = true) String phoneNumber, @RequestParam(required = true) String password){

        LoginUserRecord user = new LoginUserRecord(phoneNumber, password);
        Users loginUser = authService.login(user);

        if(loginUser != null){
            String jwtToken = jwtUtilities.generateToken(user.phoneNumber(), null);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.phoneNumber());

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(SecurityConfigConst.JWT_TOKEN, jwtToken);
            headers.add(SecurityConfigConst.REFRESH_TOKEN, refreshToken.getToken());

            return createResponse(
                    true, HttpStatus.OK,
                    headers,
                    new UserInfoResponse(
                            loginUser.getId(),
                            loginUser.getUsername(), loginUser.getFirstName(),
                            loginUser.getLastName(), loginUser.getPhoneNumber(),
                            loginUser.getMail(), loginUser.getProfileImage()),
                    "Successfully login");

        }

        return null;
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String jwtToken = jwtUtilities.generateToken(user.getPhoneNumber(), null);

                        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                        headers.add(REFRESH_TOKEN, jwtToken);

                        return createResponse(
                                true, HttpStatus.OK, headers,
                                new MessageResponse("Success"),
                                "Token is refreshed successfully!"
                        );

                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in our system!"));
        }

        return createResponse(
                false, HttpStatus.BAD_REQUEST, null,
                new MessageResponse("Empty"),
                "Refresh Token is empty!"
        );
    }


}
