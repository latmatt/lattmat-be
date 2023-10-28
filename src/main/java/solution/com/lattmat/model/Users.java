package solution.com.lattmat.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import solution.com.lattmat.security.enumeration.LoginProvider;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String oauthLoginId;
    private String username;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String password;
    private String mail;
    private String profileImage;
    private boolean isActive;
    private boolean isLock;

    @Enumerated(EnumType.STRING)
    private LoginProvider provider;

    public static Users fromOidcUser(OidcUser oidcUser, LoginProvider provider) {

        return Users.builder()
                .provider(provider)
                .username(oidcUser.getFullName())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .mail(oidcUser.getEmail())
                .oauthLoginId(oidcUser.getName())
                .profileImage(oidcUser.getAttribute("picture"))
                .build();

    }

    public static Users fromOAuth2User(OAuth2User oAuth2User, LoginProvider provider) {

        return Users.builder()
                .provider(provider)
                .username(oAuth2User.getAttribute("login"))
//                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .oauthLoginId(oAuth2User.getName())
                .profileImage(oAuth2User.getAttribute("avatar_url"))
                .build();
    }
}
