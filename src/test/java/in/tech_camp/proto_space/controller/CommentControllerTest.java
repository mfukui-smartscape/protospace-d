package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.Comment;
import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.mapper.CommentMapper;
import in.tech_camp.proto_space.mapper.PrototypeMapper;
import in.tech_camp.proto_space.mapper.UserMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PrototypeMapper prototypeMapper;

    @Autowired
    CommentMapper commentMapper;

    private Long userId;
    private Long prototypeId;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setEmail("yamada@example.com");
        user.setPassword("password123");
        user.setName("山田太郎");
        user.setProfile("プロフィール");
        user.setAffiliation("TechCamp");
        user.setPosition("Engineer");

        userMapper.insert(user);
        userId = user.getId();

        Prototype prototype = new Prototype();
        prototype.setName("AIカメラ");
        prototype.setCatchCopy("未来を映す");
        prototype.setConcept("AI画像解析");
        prototype.setImageName("camera.png");
        prototype.setUserId(userId);

        prototypeMapper.insert(prototype);
        prototypeId = prototype.getId();
    }

    // ==========================
    // コメントフォーム表示
    // ==========================

    @Test
    @WithMockUser(username = "yamada@example.com")
    void ログイン時はコメント欄が表示される() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("コメント投稿")));
    }

    @Test
    void 未ログイン時はコメント欄が表示されない() throws Exception {

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("コメント投稿"))));
    }

    // ==========================
    // コメント投稿
    // ==========================

    @Test
    @WithMockUser(username = "yamada@example.com")
    void コメント投稿が成功しDBに保存される() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId + "/comments")
                .with(csrf())
                .param("content", "いいね！"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prototypes/" + prototypeId));

        Comment savedComment = commentMapper.findByPrototypeId(prototypeId);

        assertNotNull(savedComment);
        assertEquals("いいね！", savedComment.getContent());
        assertEquals(prototypeId, savedComment.getPrototypeId());
        assertEquals(userId, savedComment.getUserId());
    }

    @Test
    @WithMockUser(username = "yamada@example.com")
    void 空コメントは投稿できない() throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId + "/comments")
                .with(csrf())
                .param("content", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    // ==========================
    // コメント表示
    // ==========================

    @Test
    void DBに保存されたコメントと投稿者名が表示される()
            throws Exception {

        Comment comment = new Comment();
        comment.setContent("いいね！");
        comment.setPrototypeId(prototypeId);
        comment.setUserId(userId);

        commentMapper.insert(comment);

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("いいね！")))
                .andExpect(content().string(
                        containsString("山田太郎")));
    }

    @Test
    void 他プロトタイプのコメントは表示されない()
            throws Exception {

        Prototype otherPrototype = new Prototype();
        otherPrototype.setName("別プロトタイプ");
        otherPrototype.setCatchCopy("別キャッチコピー");
        otherPrototype.setConcept("別コンセプト");
        otherPrototype.setImageName("other.png");
        otherPrototype.setUserId(userId);

        prototypeMapper.insert(otherPrototype);

        Long otherPrototypeId = otherPrototype.getId();

        Comment otherComment = new Comment();
        otherComment.setContent("別プロトタイプのコメント");
        otherComment.setPrototypeId(otherPrototypeId);
        otherComment.setUserId(userId);

        commentMapper.insert(otherComment);

        mockMvc.perform(get("/prototypes/" + prototypeId))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        not(containsString("別プロトタイプのコメント"))));
    }

    // ==========================
    // 認可
    // ==========================

    @Test
    void 未ログインではコメント投稿できない()
            throws Exception {

        mockMvc.perform(post("/prototypes/" + prototypeId + "/comments")
                .with(csrf())
                .param("content", "テストコメント"))
                .andExpect(status().is3xxRedirection());

        Comment comment = commentMapper.findByPrototypeId(prototypeId);

        assertNull(comment);
    }
}
