package in.tech_camp.proto_space.security;

import org.springframework.security.core.userdetails
        .UserDetails;
import org.springframework.security.core.userdetails
        .UserDetailsService;
import org.springframework.security.core.userdetails
        .UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.repository.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email);
        if (user == null) {
                throw new UsernameNotFoundException(
                        "User not found");
        }
        return new CustomUserDetails(user);
    }
}