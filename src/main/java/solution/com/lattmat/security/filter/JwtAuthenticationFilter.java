package solution.com.lattmat.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import solution.com.lattmat.security.service.CustomUserDetailsService;
import solution.com.lattmat.security.utils.JwtUtilities;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtUtilities jwtUtilities;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtilities.getToken(request);
        if (token != null && jwtUtilities.validateToken(token)) {

            String phoneNumber = jwtUtilities.extractUsername(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(phoneNumber);

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername() ,null , userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                log.info("authenticated user with phoneNumber :{}", phoneNumber);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }

        filterChain.doFilter(request,response);
    }
}
