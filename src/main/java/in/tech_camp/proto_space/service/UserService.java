package in.tech_camp.proto_space.service;

import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void create(User user) {
        userRepository.insert(user);
    }
}