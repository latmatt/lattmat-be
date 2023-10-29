package solution.com.lattmat.security.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import solution.com.lattmat.constant.SecurityConstant;
import solution.com.lattmat.security.domain.JWTAccessDeniedHandler;
import solution.com.lattmat.security.domain.JWTAuthenticationEntryPoint;
import solution.com.lattmat.security.domain.OAuth2AuthenticationSuccessHandler;
import solution.com.lattmat.security.filter.JwtAuthenticationFilter;

import java.util.Arrays;

@Slf4j
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain (
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler,
            OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler
    ) throws Exception {
        return http
                .csrf(c -> c.disable())
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oc -> oc
                        .userInfoEndpoint(ui -> ui
                                .userService(oauth2LoginHandler)
                                .oidcUserService(oidcLoginHandler))
                .successHandler(oAuth2AuthenticationSuccessHandler))
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
//                        .anyRequest().authenticated())
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler ->
                        handler.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> successLogger() {
        return SecurityConfig::onApplicationEvent;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static void onApplicationEvent(AuthenticationSuccessEvent event) {
        log.info("success: {}", event.getAuthentication());
    }

}
