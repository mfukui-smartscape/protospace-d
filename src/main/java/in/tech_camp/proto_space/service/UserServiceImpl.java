
package in.tech_camp.proto_space.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.repository.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    
@Override
public User createUserWithEncryptedPassword(User user) {

    user.setPassword(
            passwordEncoder.encode(
                    user.getPassword()));

    if (user.getProfile() == null) {
        user.setProfile("");
    }

    if (user.getAffiliation() == null) {
        user.setAffiliation("");
    }

    if (user.getPosition() == null) {
        user.setPosition("");
    }

    userMapper.insert(user);

    return user;
}


    @Override
    public User findByEmail(String email) {

        return userMapper.findByEmail(email);
    }

    @Override
    public User findById(Long id) {

        return userMapper.findById(id);
    }

    
}
