package in.tech_camp.proto_space.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.repository.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createUserWithEncryptedPassword(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public UserEntity findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public UserEntity findById(Long id) {
        return userMapper.findById(id);
    }
}