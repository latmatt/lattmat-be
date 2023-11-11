//package solution.com.lattmat.security.service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import solution.com.lattmat.convertor.RoleConverter;
//import solution.com.lattmat.convertor.UserConverter;
//import solution.com.lattmat.entity.Role;
//import solution.com.lattmat.entity.Users;
//import solution.com.lattmat.enumeration.UserRoleType;
//import solution.com.lattmat.security.domain.SecurityUser;
//import solution.com.lattmat.security.enumeration.LoginProvider;
//import solution.com.lattmat.service.RoleService;
//import solution.com.lattmat.service.UserService;
//
//import java.util.Set;
//
//@Service
//@AllArgsConstructor
//public class CustomOAuthService {
//
//    private final UserService userService;
//    private final RoleService roleService;
//
//    @Bean
//    public OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {
//
//        OidcUserService oidcDelegate = new OidcUserService();
//
//        return userRequest -> {
//            LoginProvider provider = getProvider(userRequest);
//            OidcUser oidcUser = oidcDelegate.loadUser(userRequest);
//
//            Users user = Users.fromOidcUser(oidcUser, provider);
//            Role defaultRole = RoleConverter.dtoToEntity(roleService.getRoleByRoleType(UserRoleType.ROLE_NORMAL_USER));
//            user.setRoles(Set.of(defaultRole));
//
//            SecurityUser securityUser = SecurityUser
//                    .builder()
//                    .user(user)
//                    .build();
//
//            saveOauth2User(securityUser);
//
//            return oidcUser;
//        };
//    }
//
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler() {
//
//        DefaultOAuth2UserService oauth2Delegate = new DefaultOAuth2UserService();
//
//        return userRequest -> {
//            LoginProvider provider = getProvider(userRequest);
//            OAuth2User oAuth2User = oauth2Delegate.loadUser(userRequest);
//
//            Users user = Users.fromOAuth2User(oAuth2User, provider);
//            Role defaultRole = RoleConverter.dtoToEntity(roleService.getRoleByRoleType(UserRoleType.ROLE_NORMAL_USER));
//            user.setRoles(Set.of(defaultRole));
//
//            SecurityUser securityUser = SecurityUser
//                    .builder()
//                    .user(user)
//                    .build();
//
//            saveOauth2User(securityUser);
//
//            return oAuth2User;
//        };
//    }
//
//    private void saveOauth2User(SecurityUser securityUser) {
////        CompletableFuture.runAsync(() -> createUser(securityUser), executor);
//        createUser(securityUser);
//    }
//
//    @Transactional
//    protected void createUser(SecurityUser user) {
//        saveUserIfNotExists(user);
//
//    }
//
//    private Users saveUserIfNotExists(SecurityUser securityUser) {
//        return userService
//                .findUsersByLoginId(securityUser.getUser().getLoginId())
//                .orElseGet(() ->
//                        UserConverter.dtoToEntity(
//                                userService.saveUser(UserConverter.entityToDto(securityUser.getUser()))));
//    }
//
//    private LoginProvider getProvider(OAuth2UserRequest userRequest) {
//        return LoginProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
//    }
//}
