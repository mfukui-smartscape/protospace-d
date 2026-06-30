package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserDetailTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PrototypeMapper prototypeMapper;

    Long userId;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("yamada@example.com");
        user.setPassword("password123");
        user.setName("山田太郎");
        user.setProfile("エンジニアです");
        user.setAffiliation("テック株式会社");
        user.setPosition("バックエンドエンジニア");
        userMapper.insert(user);
        userId = user.getId();

        Prototype prototype = new Prototype();
        prototype.setName("テストプロトタイプ");
        prototype.setCatchCopy("すごいキャッチコピー");
        prototype.setConcept("コンセプトです");
        prototype.setImageName("sample.png");
        prototype.setUserId(userId);
        prototypeMapper.insert(prototype);
    }

    // ===== 未ログイン状態 =====
    @Nested
    class 未ログインのとき {

        @Test
        void ユーザー詳細にアクセスできる() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(status().isOk());
        }

        @Test
        void 名前が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("山田太郎")));
        }

        @Test
        void プロフィールが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("エンジニアです")));
        }

        @Test
        void 所属が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("テック株式会社")));
        }

        @Test
        void 役職が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("バックエンドエンジニア")));
        }

        @Test
        void 投稿したプロトタイプが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("テストプロトタイプ")));
        }
    }

    // ===== ログイン状態 =====
    @Nested
    @WithMockUser   // このグループ全体がログイン状態になる
    class ログイン状態のとき {

        @Test
        void ユーザー詳細にアクセスできる() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(status().isOk());
        }

        @Test
        void 名前が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("山田太郎")));
        }

        @Test
        void プロフィールが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("エンジニアです")));
        }

        @Test
        void 所属が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("テック株式会社")));
        }

        @Test
        void 役職が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("バックエンドエンジニア")));
        }

        @Test
        void 投稿したプロトタイプが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("テストプロトタイプ")));
        }
    }
}