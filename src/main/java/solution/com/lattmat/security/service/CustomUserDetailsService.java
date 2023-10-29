package solution.com.lattmat.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import solution.com.lattmat.entity.Users;
import solution.com.lattmat.security.domain.SecurityUser;
import solution.com.lattmat.service.UserService;

import java.util.concurrent.Executor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final Executor executor;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        final Users user = userService.findUsersByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user"));

        return new SecurityUser(user, null);
    }

}