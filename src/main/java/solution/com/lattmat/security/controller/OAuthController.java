package solution.com.lattmat.security.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solution.com.lattmat.controller.BaseController;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.security.config.SecurityConfigConst;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.model.UserInfoResponse;
import solution.com.lattmat.security.service.RefreshTokenService;
import solution.com.lattmat.security.utils.JwtUtilities;
import solution.com.lattmat.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/o")
public class OAuthController extends BaseController {

    private final UserService userService;
    private final JwtUtilities jwtUtilities;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/login-rediret")
    public ResponseEntity<CustomResponse<UserInfoResponse>> callBack(@RequestParam String userId){

        Users loginUser = userService.findUsersByLoginId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));

        String jwtToken = jwtUtilities.generateToken(loginUser.getLoginId(), null);
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

}
