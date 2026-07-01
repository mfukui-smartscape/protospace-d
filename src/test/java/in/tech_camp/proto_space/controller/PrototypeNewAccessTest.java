package in.tech_camp.proto_space.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PrototypeNewAccessTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 未ログインだと投稿ページにアクセスできずログインへリダイレクトされる() throws Exception {
        mockMvc.perform(get("/prototypes/new"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void ログイン状態だと投稿ページにアクセスできる() throws Exception {
        mockMvc.perform(get("/prototypes/new"))
               .andExpect(status().isOk());
    }
}