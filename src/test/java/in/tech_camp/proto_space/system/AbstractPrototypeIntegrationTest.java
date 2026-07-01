package in.tech_camp.proto_space.system;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.mapper.PrototypeMapper;
import in.tech_camp.proto_space.mapper.UserMapper;

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

    protected User user1;
    protected User user2;

    protected Prototype prototype1;
    protected Prototype prototype2;

    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setPassword("password");
        user1.setName("ユーザー1");
        userMapper.insert(user1);

        user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setPassword("password");
        user2.setName("ユーザー2");
        userMapper.insert(user2);

        prototype1 = new Prototype();
        prototype1.setName("プロトタイプ1");
        prototype1.setCatchCopy("キャッチコピー1");
        prototype1.setConcept("コンセプト1");
        prototype1.setImageName("image1.png");
        prototype1.setUserId(user1.getId());

        prototypeMapper.insert(prototype1);

        prototype2 = new Prototype();
        prototype2.setName("プロトタイプ2");
        prototype2.setCatchCopy("キャッチコピー2");
        prototype2.setConcept("コンセプト2");
        prototype2.setImageName("image2.png");
        prototype2.setUserId(user2.getId());

        prototypeMapper.insert(prototype2);
    }
}