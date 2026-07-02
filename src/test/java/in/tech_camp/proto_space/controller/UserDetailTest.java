package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
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
    @Transactional
    class 未ログインのとき {

        @Test
        void ユーザー詳細にアクセスできる() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(status().isOk());
        }

        @Test
	void 名前が正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-name']").string("山田太郎"));
        }

        @Test
        void プロフィールが正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-profile']").string("エンジニアです"));
        }

        @Test
        void 所属が正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-affiliation']").string("テック株式会社"));
        }

        @Test
        void 役職が正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-position']").string("バックエンドエンジニア"));
        }

        @Test
        void 投稿したプロトタイプ名が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='prototype-name']").string("テストプロトタイプ"));
        }

        @Test
        void プロトタイプ画像タグが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//img[@data-testid='prototype-image']").exists())
                   .andExpect(xpath("//img[@data-testid='prototype-image']/@src")
                              .string(containsString("sample.png")));
        }

        @Test
        void 名前が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-name']").string("山田太郎"));
        }

        @Test
        void プロフィールが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-profile']").string("エンジニアです"));
        }

        @Test
        void 所属が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-affiliation']").string("テック株式会社"));
        }

        @Test
        void 役職が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-position']").string("バックエンドエンジニア"));
        }

        @Test
        void 投稿したプロトタイプが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='prototype-name']").string("テストプロトタイプ"));
        }
    }

    // ===== ログイン状態 =====
    @Nested
    @WithMockUser
    @Transactional

    class ログイン状態のとき {

        @Test
        void ユーザー詳細にアクセスできる() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(status().isOk());
        }

        @Test
        void 名前が正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-name']").string("山田太郎"));
        }

        @Test
        void プロフィールが正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-profile']").string("エンジニアです"));
        }

        @Test
        void 所属が正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-affiliation']").string("テック株式会社"));
        }

        @Test
        void 役職が正しい要素に表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-position']").string("バックエンドエンジニア"));
        }

        @Test
        void 投稿したプロトタイプ名が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='prototype-name']").string("テストプロトタイプ"));
        }

        @Test
        void プロトタイプ画像タグが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//img[@data-testid='prototype-image']").exists())
                   .andExpect(xpath("//img[@data-testid='prototype-image']/@src")
                              .string(containsString("sample.png")));
        }
    }

    // ===== 画像ファイルが実際に配信されるか =====
    // 認証と無関係な静的ファイル配信の検証なので、独立したNestedに分離
    @Nested
    class 静的ファイル配信 {

        @Test
        void プロトタイプ画像が配信される() throws Exception {
            mockMvc.perform(get("/images/sample.png"))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.IMAGE_PNG));
        }
        void 名前が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-name']").string("山田太郎"));
        }

        @Test
        void プロフィールが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-profile']").string("エンジニアです"));
        }

        @Test
        void 所属が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-affiliation']").string("テック株式会社"));
        }

        @Test
        void 役職が表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(xpath("//*[@data-testid='user-position']").string("バックエンドエンジニア"));
        }

        @Test
        void 投稿したプロトタイプが表示される() throws Exception {
            mockMvc.perform(get("/users/" + userId))
                   .andExpect(content().string(containsString("テストプロトタイプ")));
        }
    }
}