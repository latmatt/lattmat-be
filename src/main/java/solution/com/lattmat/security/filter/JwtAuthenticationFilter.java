package solution.com.lattmat.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import solution.com.lattmat.constant.MessageConstant;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.security.service.CustomUserDetailsService;
import solution.com.lattmat.security.utils.JwtUtilities;

import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtUtilities jwtUtilities;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtilities.getToken(request);
        boolean isValidToken = false;

        try {
            isValidToken = jwtUtilities.validateToken(token);

            if (token != null && isValidToken) {

                String loginId = jwtUtilities.extractLoginId(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    log.info("authenticated user with phoneNumber :{}", loginId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            } else {
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            sendResponse(response, FORBIDDEN, MessageConstant.JWT_TOKEN_EXPIRED_MESSAGE);
        }
    }

    private void sendResponse(HttpServletResponse response, HttpStatus status, String msg) throws IOException {
        CustomResponse httpResponse = CustomResponse.builder()
                .success(false).code(status.value()).data(null).message(msg).build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
