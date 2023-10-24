package solution.com.lattmat.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.domain.SecurityUser;
import solution.com.lattmat.service.UserService;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        final Users userByPhoneNumber = userService.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user"));

        return new SecurityUser(userByPhoneNumber);
    }
}