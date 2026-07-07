package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.repository.PrototypeMapper;
import in.tech_camp.proto_space.repository.UserMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TopPageTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper UserMapper;

    @Autowired
    PrototypeMapper PrototypeMapper;

    Long userId;
    Long prototypeId;
    String userName;

    @BeforeEach
    void setUp() {
        // 投稿者
        UserEntity user = new UserEntity();
        user.setEmail("yamada@example.com");
        user.setPassword("password123");
        user.setName("山田太郎");
        user.setProfile("エンジニアです");
        user.setAffiliation("テック株式会社");
        user.setPosition("バックエンドエンジニア");
        UserMapper.insert(user);
        userId = user.getId();
        userName = user.getName();

        // 投稿（トップに表示される）
        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setName("テストプロトタイプ");
        prototype.setCatchCopy("すごいキャッチコピー");
        prototype.setConcept("コンセプトです");
        prototype.setImageName("sample.png");
        prototype.setUserId(userId);
        PrototypeMapper.insert(prototype);
        prototypeId = prototype.getId();
    }

    // ===== ログイン状態に関わらず表示される =====

    @Test
    void トップページにアクセスできる() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }

    @Test
    void プロトタイプ名が正しい要素に表示される() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(xpath("//*[@data-testid='prototype-name']")
                    .string("テストプロトタイプ"));
    }
    @Test
    void キャッチコピーが表示される() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(xpath("//*[@data-testid='prototype-catch-copy']")
                    .string("すごいキャッチコピー"));
    }

    @Test
    void 投稿者の名前が表示される() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(xpath("//*[@data-testid='user-name']")
                    .string(org.hamcrest.Matchers.containsString("山田太郎")));
    }

    @Test
    void 画像のimgタグが出力されている() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(xpath("//img[@data-testid='prototype-image']")
               .exists());
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
    @WithUserDetails(value = "yamada@example.com",
                    setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void ログイン状態だとこんにちはとユーザー名が表示される() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(xpath("//*[@data-testid='welcome-message']")
                    .string("こんにちは、" + userName + "です"));
    }
}