package solution.com.lattmat.security.domain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.service.RefreshTokenService;
import solution.com.lattmat.security.utils.CookieUtils;
import solution.com.lattmat.security.utils.JwtUtilities;

import java.io.IOException;
import java.util.Optional;

import static solution.com.lattmat.constant.OAuth2constant.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RefreshScope
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${redirectUrl:http://localhost:3000}")
    private String defaultRedirectUri;

    private final JwtUtilities jwtUtilities;
    private final RefreshTokenService refreshTokenService;

    public OAuth2AuthenticationSuccessHandler(JwtUtilities jwtUtilities, RefreshTokenService refreshTokenService){
        this.jwtUtilities = jwtUtilities;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

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

}