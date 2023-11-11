package solution.com.lattmat.security.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import solution.com.lattmat.controller.BaseController;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.domain.MessageResponse;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.exception.domain.InvalidCredentialsException;
import solution.com.lattmat.exception.domain.TokenRefreshException;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.security.config.SecurityConfigConst;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.model.LoginUserRecord;
import solution.com.lattmat.security.model.SignUpUserRecord;
import solution.com.lattmat.security.model.UserInfoResponse;
import solution.com.lattmat.security.service.AuthService;
import solution.com.lattmat.security.service.RefreshTokenService;
import solution.com.lattmat.security.utils.JwtUtilities;
import solution.com.lattmat.service.UserService;

import java.net.http.HttpResponse;
import java.util.UUID;

import static solution.com.lattmat.security.config.SecurityConfigConst.REFRESH_TOKEN;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    private final JwtUtilities jwtUtilities;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<UserInfoResponse>> register(@RequestBody SignUpUserRecord user){

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
    @RateLimiter(name = "basic")
    public ResponseEntity<CustomResponse<UserInfoResponse>> login(@RequestParam(required = true) String phoneNumber, @RequestParam(required = true) String password){

        LoginUserRecord user = new LoginUserRecord(phoneNumber, password);
        Users loginUser = authService.login(user);

        if(loginUser != null){
            String jwtToken = jwtUtilities.generateToken(loginUser.getId().toString(), null);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginUser.getLoginId());

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

    @GetMapping("/me")
    public ResponseEntity<CustomResponse<UserInfoResponse>> getCurrentUser(HttpServletRequest request) {

        String jwtToken = jwtUtilities.getToken(request);
        String loginId = jwtUtilities.extractLoginId(jwtToken);

        final Users user = userService.findUsersById(UUID.fromString(loginId))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));

        return createResponse(
                true, HttpStatus.OK,
                null,
                new UserInfoResponse(
                        user.getId(),
                        user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getPhoneNumber(),
                        user.getMail(), user.getProfileImage()),
                "Successfully login");

    }

    public ResponseEntity<CustomResponse<UserInfoResponse>> basicFallBack(
            String phoneNumber, String password, Throwable t){
        System.out.println("LIMITED RATE");
        System.out.println(t.getMessage());
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
                        System.out.println("Generating token");
                        String jwtToken = jwtUtilities.generateToken(user.getLoginId(), null);

                        System.out.println(jwtToken);
                        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                        headers.add(REFRESH_TOKEN, jwtToken);

                        return createResponse(
                                true, HttpStatus.OK, headers,
                                new MessageResponse("Success"),
                                "Token is refreshed successfully!"
                        );

                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Invalid request while refreshing token"));
        }

        return createResponse(
                false, HttpStatus.BAD_REQUEST, null,
                new MessageResponse("Empty"),
                "Refresh Token is empty!");
    }

    @PostMapping("/change-password")
    public ResponseEntity<CustomResponse<MessageResponse>> changePassword(
            @RequestParam(required = true) String oldPassword, @RequestParam(required = true) String newPassword, @RequestParam(required = true) String phoneNumber ){

        Users user = userService.findUsersByLoginId(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

        if(passwordEncoder.matches(oldPassword, user.getPassword())) {
            authService.changePassword(newPassword, user);
        } else {
            throw new InvalidCredentialsException("Your password is incorrect");
        }

        return createResponse(
                true, HttpStatus.OK, null,
                new MessageResponse("Success"),
                "Password changed successfully...");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<CustomResponse<MessageResponse>> resetPassword(
            @RequestParam(required = true) String newPassword, @RequestParam(required = true) String phoneNumber){

        Users user = userService.findUsersByLoginId(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

        authService.changePassword(newPassword, user);

        return createResponse(
                true, HttpStatus.OK, null,
                new MessageResponse("Success"),
                "Password changed successfully...");

    }

}
