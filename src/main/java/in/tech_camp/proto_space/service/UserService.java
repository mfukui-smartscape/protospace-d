package in.tech_camp.proto_space.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.repositry.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * パスワードをハッシュ化して保存する
     */
    public void createUserWithEncryptedPassword(User user) {
        String encryptedPassword =passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userMapper.insert(user);
    }
}