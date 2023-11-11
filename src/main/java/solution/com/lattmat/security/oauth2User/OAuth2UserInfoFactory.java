package solution.com.lattmat.security.oauth2User;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import solution.com.lattmat.security.enumeration.LoginProvider;

import java.util.Locale;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo of(OAuth2AuthenticationToken authenticationToken) {
        final Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();
        LoginProvider provider = LoginProvider.valueOf(
                authenticationToken.getAuthorizedClientRegistrationId().toUpperCase(Locale.ROOT));

        return switch (provider){
            case APP -> null;
            case FACEBOOK -> new FacebookOAuth2UserInfo(attributes);
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
        };
    }

}
