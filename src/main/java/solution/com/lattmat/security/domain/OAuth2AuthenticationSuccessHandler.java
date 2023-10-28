package solution.com.lattmat.security.domain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();

        redirectUri = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("userId", user.getName())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

}