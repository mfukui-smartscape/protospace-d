package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TopPageTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PrototypeMapper prototypeMapper;

    Long userId;
    Long prototypeId;

    @BeforeEach
    void setUp() {
        // 投稿者
        User user = new User();
        user.setEmail("yamada@example.com");
        user.setPassword("password123");
        user.setName("山田太郎");
        user.setProfile("エンジニアです");
        user.setAffiliation("テック株式会社");
        user.setPosition("バックエンドエンジニア");
        userMapper.insert(user);
        userId = user.getId();

        // 投稿（トップに表示される）
        Prototype prototype = new Prototype();
        prototype.setName("テストプロトタイプ");
        prototype.setCatchCopy("すごいキャッチコピー");
        prototype.setConcept("コンセプトです");
        prototype.setImageName("sample.png");
        prototype.setUserId(userId);
        prototypeMapper.insert(prototype);
        prototypeId = prototype.getId();
    }

    // ===== ログイン状態に関わらず表示される =====

    @Test
    void トップページにアクセスできる() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }

    @Test
    void プロトタイプ名が表示される() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("テストプロトタイプ")));
    }

    @Test
    void キャッチコピーが表示される() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("すごいキャッチコピー")));
    }

    @Test
    void 投稿者の名前が表示される() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("山田太郎")));
    }

    @Test
    void 画像のimgタグが出力されている() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("sample.png")));
    }

    @Test
    void 詳細ページへのリンクがある() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("/prototypes/" + prototypeId)));
    }

    @Test
    void ユーザー詳細へのリンクがある() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("/users/" + userId)));
    }

    // ===== ログイン状態でのみ表示される =====

    @Test
    @WithMockUser(username = "yamada@example.com")
    void ログイン状態だとこんにちはとユーザー名が表示される() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(content().string(containsString("こんにちは")));
    }
}