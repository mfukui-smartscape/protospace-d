package in.tech_camp.proto_space.system;


import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.repositories.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional

public class UserSignUpIntegrationTest {
  
  @Autowired
  private UserRepository userRepository;

  @Test
  void メールが重複すると保存できない() {

      // ① 1件目のユーザーを保存（成功）
      User user1 = new User();
      user1.setEmail("test@test.com");
      user1.setPassword("123456");
      userRepository.save(user1);

        // ② 同じメールで2件目を作る
      User user2 = new User();
      user2.setEmail("test@test.com");
      user2.setPassword("123456");

      // ③ 保存すると例外が発生することを確認
      assertThrows(Exception.class, () -> {
          userRepository.save(user2);
      });
  }
}
