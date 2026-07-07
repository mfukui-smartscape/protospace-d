package in.tech_camp.proto_space.system;


import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.repository.UserMapper;


@Transactional
@SpringBootTest
public class UserSignUpIntegrationTest {
  
  @Autowired
  private UserMapper userMapper;

  @Test
  void メールが重複すると保存できない() {

      // ① 1件目のユーザーを保存（成功）
      User user1 = new User();
      user1.setEmail("test@test.com");
      user1.setPassword("123456");
      user1.setName("山田太郎");
      user1.setProfile("プロフィール");
      user1.setAffiliation("所属");
      user1.setPosition("職種");
      userMapper.insert(user1);

        // ② 同じメールで2件目を作る
      User user2 = new User();
      user2.setEmail("test@test.com");
      user2.setPassword("123456");
      user2.setName("山田太郎");
      user2.setProfile("プロフィール");
      user2.setAffiliation("所属");
      user2.setPosition("職種");
      // ③ 保存すると例外が発生することを確認
      assertThrows( DataIntegrityViolationException.class, () -> {
          userMapper.insert(user2);
      });
  }
}
