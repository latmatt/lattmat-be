package solution.com.lattmat.security.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solution.com.lattmat.convertor.UserConverter;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.domain.SecurityUser;
import solution.com.lattmat.security.enumeration.LoginProvider;
import solution.com.lattmat.service.UserService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final Executor executor;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        final Users userByPhoneNumber = userService.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user"));

        return new SecurityUser(userByPhoneNumber, null, null);
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {

        OidcUserService oidcDelegate = new OidcUserService();

        return userRequest -> {
            LoginProvider provider = getProvider(userRequest);
            OidcUser oidcUser = oidcDelegate.loadUser(userRequest);

            SecurityUser securityUser = SecurityUser
                    .builder()
                    .user(Users.fromOidcUser(oidcUser, provider))
//                    .attributes(oidcUser.getAttributes())
//                    .authorities(oidcUser.getAuthorities())
                    .build();

            saveOauth2User(securityUser);

            return oidcUser;
        };
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler() {

        DefaultOAuth2UserService oauth2Delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            LoginProvider provider = getProvider(userRequest);
            OAuth2User oAuth2User = oauth2Delegate.loadUser(userRequest);

            SecurityUser securityUser = SecurityUser
                    .builder()
                    .user(Users.fromOAuth2User(oAuth2User, provider))
//                    .attributes(oAuth2User.getAttributes())
//                    .authorities(oAuth2User.getAuthorities())
                    .build();

            saveOauth2User(securityUser);

            System.out.println("OAUTH USER - ");
            System.out.println(securityUser);

            return oAuth2User;
        };
    }

    private void saveOauth2User(SecurityUser securityUser) {
        CompletableFuture.runAsync(() -> createUser(securityUser), executor);
    }

    @Transactional
    protected void createUser(SecurityUser user) {
        Users userEntity = saveUserIfNotExists(user);

//        List<AuthorityEntity> authorities = user
//                .authorities
//                .stream()
//                .map(a -> saveAuthorityIfNotExists(a.getAuthority(), user.getProvider()))
//                .toList();
//
//        userEntity.mergeAuthorities(authorities);

//        userEntityRepository.save(userEntity);

    }

    private Users saveUserIfNotExists(SecurityUser securityUser) {
        return userService
                .findUsersByOauthLoginId(securityUser.getUser().getOauthLoginId())
                .orElseGet(() ->
                        UserConverter.dtoToEntity(
                                userService.saveUser(UserConverter.entityToDto(securityUser.getUser()))));
    }

    private LoginProvider getProvider(OAuth2UserRequest userRequest) {
        return LoginProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
    }

}