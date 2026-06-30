package in.tech_camp.proto_space.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.mapper.PrototypeMapper;
import in.tech_camp.proto_space.mapper.UserMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrototypeEditAccessTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PrototypeMapper prototypeMapper;

    Long ownerId;        // 投稿の持ち主（山田）
    Long othersPrototypeId;  // 山田が投稿したプロトタイプ

    @BeforeEach
    void setUp() {
        // 投稿の持ち主：山田太郎
        User owner = new User();
        owner.setEmail("yamada@example.com");
        owner.setPassword("password123");
        owner.setName("山田太郎");
        owner.setProfile("プロフィール");
        owner.setAffiliation("所属");
        owner.setPosition("役職");
        userMapper.insert(owner);
        ownerId = owner.getId();

        // 別人：佐藤花子（この人でログインして山田の投稿を編集しようとする）
        User other = new User();
        other.setEmail("sato@example.com");
        other.setPassword("password123");
        other.setName("佐藤花子");
        other.setProfile("プロフィール");
        other.setAffiliation("所属");
        other.setPosition("役職");
        userMapper.insert(other);

        // 山田が投稿したプロトタイプ
        Prototype prototype = new Prototype();
        prototype.setName("山田のプロトタイプ");
        prototype.setCatchCopy("キャッチコピー");
        prototype.setConcept("コンセプト");
        prototype.setImageName("sample.png");
        prototype.setUserId(ownerId);
        prototypeMapper.insert(prototype);
        othersPrototypeId = prototype.getId();
    }

    @Test
    void 未ログインだと編集ページにアクセスできずリダイレクトされる() throws Exception {
        mockMvc.perform(get("/prototypes/" + othersPrototypeId + "/edit"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "sato@example.com")  // 佐藤でログイン
    void 他人の投稿の編集ページにはアクセスできない() throws Exception {
        // 佐藤がログイン中に、山田の投稿の編集ページを直打ち
        mockMvc.perform(get("/prototypes/" + othersPrototypeId + "/edit"))
               .andExpect(status().is3xxRedirection());  // トップ等へ弾かれる
    }

    @Test
    @WithMockUser(username = "yamada@example.com")  // 本人（山田）でログイン
    void 本人なら自分の投稿の編集ページにアクセスできる() throws Exception {
        mockMvc.perform(get("/prototypes/" + othersPrototypeId + "/edit"))
               .andExpect(status().isOk());  // 本人は入れる
    }
}