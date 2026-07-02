package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.mapper.PrototypeMapper;
import in.tech_camp.proto_space.mapper.UserMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrototypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PrototypeMapper prototypeMapper;

    Long ownerId;
    Long otherUserId;
    Long prototypeId;

    @BeforeEach
    void setUp() {

        User owner = new User();
        owner.setEmail("owner@test.com");
        owner.setPassword("password");
        owner.setName("投稿者");
        owner.setProfile("オーナープロフィール");
        owner.setAffiliation("TechCamp");
        owner.setPosition("Engineer");

        userMapper.insert(owner);
        ownerId = owner.getId();

        User other = new User();
        other.setEmail("other@test.com");
        other.setPassword("password");
        other.setName("他ユーザー");
        other.setProfile("other");
        other.setAffiliation("other");
        other.setPosition("other");

        userMapper.insert(other);
        otherUserId = other.getId();

        Prototype prototype = new Prototype();
        prototype.setName("AIカメラ");
        prototype.setCatchCopy("未来を映す");
        prototype.setConcept("AIで画像解析");
        prototype.setImageName("camera.jpg");
        prototype.setUserId(ownerId);

        prototypeMapper.insert(prototype);
        prototypeId = prototype.getId();
    }

    // ======================
    // 詳細表示
    // ======================

    @Test
    void 未ログインでも詳細ページが見られる() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk());
    }

    @Test
    void DBのプロトタイプ名が表示される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(content().string(containsString("AIカメラ")));
    }

    @Test
    void DBのキャッチコピーが表示される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(content().string(containsString("未来を映す")));
    }

    @Test
    void DBのコンセプトが表示される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(content().string(containsString("AIで画像解析")));
    }

    @Test
    void DBの投稿者名が表示される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(content().string(containsString("投稿者")));
    }

    @Test
    void 画像名が出力される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(content().string(containsString("camera.jpg"))); 
    }

    // ======================
    // リンク表示
    // ======================

    @Test
    @WithMockUser(username = "owner@test.com")
    void 投稿者本人は編集リンクが見える() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/prototypes/" + prototypeId + "/edit\"")));
    }

    @Test
    @WithMockUser(username = "owner@test.com")
    void 投稿者本人は削除リンクが見える() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/prototypes/" + prototypeId + "/delete\"")));
    }

    @Test
    void 未ログインでは編集リンクが表示されない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("href=\"/prototypes/" + prototypeId + "/edit\""))));
    }

    @Test
    void 未ログインでは削除リンクが表示されない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("href=\"/prototypes/" + prototypeId + "/delete\""))));
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void 他人には編集リンクが表示されない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("href=\"/prototypes/" + prototypeId + "/edit\""))));
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void 他人には削除リンクが表示されない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("href=\"/prototypes/" + prototypeId + "/delete\""))));
    }

    // ======================
    // 編集画面
    // ======================

    @Test
    @WithMockUser(username = "owner@test.com")
    void 投稿者本人は編集画面に遷移できる() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId + "/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "owner@test.com")
    void 編集画面に既存情報が表示される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("AIカメラ")))
                .andExpect(content().string(containsString("未来を映す")))
                .andExpect(content().string(containsString("AIで画像解析")));
    }

    @Test
    void 未ログインは編集画面に遷移できない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId + "/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void 他人の投稿編集画面には遷移できない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId + "/edit"))
                .andExpect(status().is3xxRedirection());
    }

    // ======================
    // 更新
    // ======================

    @Test
    @WithMockUser(username = "owner@test.com")
    void 正常入力なら更新できる() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId)
                .with(csrf())
                .param("name", "更新後タイトル")
                .param("catchCopy", "更新後キャッチ")
                .param("concept", "更新後コンセプト"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prototypes/" + prototypeId));

        Prototype updated = prototypeMapper.findById(prototypeId);

        assertEquals("更新後タイトル", updated.getName());

        assertEquals("更新後コンセプト", updated.getConcept());
    }

    @Test
    @WithMockUser(username = "owner@test.com")
    void 空入力では更新できず編集画面に留まる() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId)
                .with(csrf())
                .param("name", "")
                .param("concept", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(username = "owner@test.com")
    void 編集失敗時も入力済み項目は保持される() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId)
                .with(csrf())
                .param("name", "入力済みタイトル")
                .param("catchCopy", "入力済みキャッチ")
                .param("concept", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(content().string(
                        containsString("入力済みタイトル")))
                .andExpect(content().string(
                        containsString("入力済みキャッチ")));
    }

    // ======================
    // 削除
    // ======================

    @Test
    @WithMockUser(username = "owner@test.com")
    void 削除するとDBから消える() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Prototype deleted = prototypeMapper.findById(prototypeId);

        assertNull(deleted);
    }

    @Test
    void 未ログインは削除できない() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());

        Prototype prototype = prototypeMapper.findById(prototypeId);

        assertNotNull(prototype);
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void 他人は削除できない() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());

        Prototype prototype = prototypeMapper.findById(prototypeId);

        assertNotNull(prototype);
    }
}