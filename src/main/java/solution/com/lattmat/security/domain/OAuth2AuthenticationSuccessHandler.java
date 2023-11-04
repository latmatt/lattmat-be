package solution.com.lattmat.security.domain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.service.RefreshTokenService;
import solution.com.lattmat.security.utils.JwtUtilities;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    private final JwtUtilities jwtUtilities;
    private final RefreshTokenService refreshTokenService;

    public OAuth2AuthenticationSuccessHandler(JwtUtilities jwtUtilities, RefreshTokenService refreshTokenService){
        this.jwtUtilities = jwtUtilities;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String responseUri = redirectUri;

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        String jwtToken = jwtUtilities.generateToken(user.getName(), null);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getName());

        responseUri = UriComponentsBuilder.fromUriString(responseUri)
                .queryParam("token", jwtToken)
                .queryParam("refreshToken", refreshToken.getToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, responseUri);
    }

}