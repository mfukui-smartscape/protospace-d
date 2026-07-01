package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  //ログイン時のみコメント欄表示
  @Test
  @WithMockUser
  void ログイン時はコメント欄が表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("コメント投稿")));
  }

  //未ログイン時はコメント欄表示されない
  @Test
  void 未ログイン時はコメント欄が表示されない() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(not(containsString("コメント投稿"))));
  }

  //コメント投稿成功
  @Test
  @WithMockUser
  void コメント投稿が成功する() throws Exception {
    mockMvc.perform(post("/prototypes/1/comments")
            .with(csrf())
            .param("content", "いいね！"))
            .andExpect(status().is3xxRedirection());
  }

  //コメント投稿失敗（空）
  @Test
  @WithMockUser
  void 空のコメントは投稿できない() throws Exception {
    mockMvc.perform(post("/prototypes/1/comments")
            .with(csrf())
            .param("content", ""))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors());
  }

  //コメントと投稿者名表示
  @Test
  @WithMockUser
  void コメントと投稿者名が表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("いいね！")))
            .andExpect(content().string(containsString("投稿者名")));
  }

  //未ログインではコメント投稿不可
  @Test
  void 未ログインではコメント投稿できない() throws Exception {
    mockMvc.perform(post("/prototypes/1/comments")
            .with(csrf())
            .param("content", "テストコメント"))
            .andExpect(status().is3xxRedirection());
  }
}
