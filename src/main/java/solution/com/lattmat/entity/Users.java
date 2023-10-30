package solution.com.lattmat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import solution.com.lattmat.enumeration.UserRoleType;
import solution.com.lattmat.security.enumeration.LoginProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String loginId;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public static Users fromOidcUser(OidcUser oidcUser, LoginProvider provider) {

        return Users.builder()
                .provider(provider)
                .username(oidcUser.getFullName())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .mail(oidcUser.getEmail())
                .loginId(oidcUser.getName())
                .profileImage(oidcUser.getAttribute("picture"))
                .build();

    }

    public static Users fromOAuth2User(OAuth2User oAuth2User, LoginProvider provider) {

        return Users.builder()
                .provider(provider)
                .username(oAuth2User.getAttribute("login"))
//                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .loginId(oAuth2User.getName())
                .profileImage(oAuth2User.getAttribute("avatar_url"))
                .build();
    }

    public void addRole(Role role) {
        if(roles.isEmpty()){
           roles = new HashSet<>();
        }

        roles.add(role);
    }
}
