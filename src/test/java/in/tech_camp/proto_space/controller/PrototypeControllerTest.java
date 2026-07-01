package in.tech_camp.proto_space.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class PrototypeControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  //プロトタイプ詳細は誰でも見れる
  @Test
  void ログインしていなくても詳細ページは表示できる() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk());
  }

  //詳細ページ表示（プロトタイプ）
  @Test
  void 詳細ページにプロトタイプ名が表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("プロトタイプ名")));
  }

  //詳細ページ表示（投稿者名）
  @Test
  void 詳細ページに投稿者名が表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("投稿者名")));
  }

  //詳細ページ表示（キャッチコピー）
  @Test
  void 詳細ページにキャチコピーが表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("キャッチコピー")));
  }

  //詳細ページ表示（コンセプト）
  @Test
  void 詳細ページにコンセプトが表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(content().string(containsString("コンセプト")));
  }

  //詳細ページ表示（画像）
  @Test
  void 詳細ページに画像が表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(content().string(containsString("<img")));
  }

  //ログイン時のみ編集リンク表示
  @Test
  @WithMockUser
  void ログイン時は編集リンクが表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("編集")));
  }

  //ログイン時のみ削除リンク表示
  @Test
  @WithMockUser
  void ログイン時は削除リンクが表示される() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("削除")));
  }

  //未ログイン時は編集リンクなし
  @Test
  void 未ログイン時は編集リンクが表示されない() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(not(containsString("編集"))));
  }

  //未ログイン時は削除リンクなし
  @Test
  void 未ログイン時は削除リンクが表示されない() throws Exception {
    mockMvc.perform(get("/prototypes/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(not(containsString("削除"))));
  }

  //編集ページ遷移
  @Test
  @WithMockUser
  void 編集ページへ遷移できる() throws Exception {
    mockMvc.perform(get("/prototypes/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("prototypes/edit"));
  }

  //未ログイン時は編集画面へ遷移できない
  @Test
  void 未ログイン時は編集ページへアクセスできない() throws Exception {
    mockMvc.perform(get("/prototypes/1/edit"))
            .andExpect(status().is3xxRedirection());
  }

  //入力済み項目が消えない
  @Test
  @WithMockUser
  void 編集失敗時も入力済み内容は保持される() throws Exception {
    mockMvc.perform(get("/prototypes/1/edit"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("prototype"));
  }

  //編集失敗（空欄）
  @Test
  @WithMockUser
  void 空入力だと編集できない() throws Exception {
    mockMvc.perform(post("/prototypes/1")
            .with(csrf())
            .param("name", "")
            .param("concept", ""))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors());
  }

  //入力内容の保存
  @Test
  @WithMockUser
  void 編集失敗時も入力済み内容は保存される() throws Exception {
    mockMvc.perform(post("/prototypes/1")
            .with(csrf())
            .param("name", "テスト名")
            .param("concept", ""))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("prototype"));
  }

  //編集成功
  @Test
  @WithMockUser
  void 正しく入力すると編集成功する() throws Exception {
    mockMvc.perform(post("/prototypes/1")
            .with(csrf())
            .param("name", "テスト")
            .param("concept", "コンセプト"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/prototypes/1"));
  }

  //削除処理
  @Test
  @WithMockUser
  void 削除するとトップへ遷移する() throws Exception {
    mockMvc.perform(post("/prototypes/1/delete")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
  }

  @Test
  void 未ログインでは削除できない() throws Exception {
    mockMvc.perform(post("/prototypes/1/delete")
            .with(csrf()))
            .andExpect(status().is3xxRedirection());
  }
  
}
