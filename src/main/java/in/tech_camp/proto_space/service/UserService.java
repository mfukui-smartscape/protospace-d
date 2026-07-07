package in.tech_camp.proto_space.service;

import in.tech_camp.proto_space.entity.UserEntity;

public interface UserService {

    UserEntity createUserWithEncryptedPassword(UserEntity user);

    UserEntity findByEmail(String email);

    UserEntity   findById(Long id);
}
