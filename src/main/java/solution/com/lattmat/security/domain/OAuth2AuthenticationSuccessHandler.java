package solution.com.lattmat.security.domain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.repo.UserRepository;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.oauth2User.OAuth2UserInfo;
import solution.com.lattmat.security.oauth2User.OAuth2UserInfoFactory;
import solution.com.lattmat.security.service.AuthService;
import solution.com.lattmat.security.service.RefreshTokenService;
import solution.com.lattmat.security.utils.CookieUtils;
import solution.com.lattmat.security.utils.JwtUtilities;
import solution.com.lattmat.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static solution.com.lattmat.constant.OAuth2constant.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RefreshScope
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${redirectUrl:http://localhost:3000/oauth-redirect}")
    private String defaultRedirectUri;

    private final JwtUtilities jwtUtilities;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final AuthService authService;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public OAuth2AuthenticationSuccessHandler(JwtUtilities jwtUtilities, RefreshTokenService refreshTokenService,
                                              UserService userService, AuthService authService, UserRepository userRepository, ModelMapper modelMapper){
        this.jwtUtilities = jwtUtilities;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        processOAuth2User(authentication);

        String redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue).orElseGet(() -> defaultRedirectUri);

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        String jwtToken = jwtUtilities.generateToken(user.getName(), null);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getName());

        String responseUri = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", jwtToken)
                .queryParam("refreshToken", refreshToken.getToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, responseUri);
    }

    private void processOAuth2User(Authentication authentication) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.of(oAuth2AuthenticationToken);

        userService.findUsersByLoginId(userInfo.getId())
                .ifPresentOrElse(user -> update(user, userInfo, principal.getAttributes()), () -> register(userInfo, principal.getAttributes()));

    }

    private void update(Users user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setUsername(userInfo.getName());
        user.setMail(userInfo.getEmail());
        user.setProfileImage(userInfo.getPicture());
        user.setLoginId(userInfo.getId());
        user.setProvider(userInfo.getLoginProvider());

        userRepository.save(user);

        saveAuthContext(user, userInfo, attributes);

    }

    private void register(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        final UserDto userDto = authService.register(userInfo);
        Users user = modelMapper.map(userDto, Users.class);

        saveAuthContext(user, userInfo, attributes);
    }

    private void saveAuthContext(Users user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        SecurityUser securityUser = new SecurityUser(user, attributes);

        Authentication securityAuth = new OAuth2AuthenticationToken(securityUser,
                user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleType().name())).toList(), userInfo.getLoginProvider().name());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }



}