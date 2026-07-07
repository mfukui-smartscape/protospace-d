
package in.tech_camp.proto_space.service;

import in.tech_camp.proto_space.entity.User;

public interface UserService {

    User createUserWithEncryptedPassword(User user);

    User findByEmail(String email);

    User findById(Long id);
}
