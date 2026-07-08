package in.tech_camp.proto_space.system;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.repository.PrototypeMapper;
import in.tech_camp.proto_space.repository.UserMapper;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractPrototypeIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected PrototypeMapper prototypeMapper;

    protected UserEntity user1;
    protected UserEntity user2;

    protected PrototypeEntity prototype1;
    protected PrototypeEntity prototype2;

    @BeforeEach
    void setUp() {

 
        user1 = new UserEntity();
        user1.setEmail("user1@test.com");
    user1.setPassword("password");
    user1.setName("ユーザー1");
    user1.setProfile("");
user1.setAffiliation("");
user1.setPosition("");
userMapper.insert(user1);

user2 = new UserEntity();
user2.setEmail("user2@test.com");
user2.setPassword("password");
user2.setName("ユーザー2");
user2.setProfile("");
user2.setAffiliation("");
user2.setPosition("");
userMapper.insert(user2);
;

        prototype1 = new PrototypeEntity();
        prototype1.setName("プロトタイプ1");
        prototype1.setCatchCopy("キャッチコピー1");
        prototype1.setConcept("コンセプト1");
        prototype1.setImageName("image1.png");
        prototype1.setUserId(user1.getId());

        prototypeMapper.insert(prototype1);

        prototype2 = new PrototypeEntity();
        prototype2.setName("プロトタイプ2");
        prototype2.setCatchCopy("キャッチコピー2");
        prototype2.setConcept("コンセプト2");
        prototype2.setImageName("image2.png");
        prototype2.setUserId(user2.getId());

        prototypeMapper.insert(prototype2);
    }
}