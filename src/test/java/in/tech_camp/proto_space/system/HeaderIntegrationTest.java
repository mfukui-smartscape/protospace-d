package in.tech_camp.proto_space.system;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HeaderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 未ログイン時はヘッダーに新規登録とログインが表示される() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ログイン")))
                .andExpect(content().string(containsString("新規登録")));
    }

    @Test
    @WithMockUser(username = "user1@test.com")
    void ログイン時はヘッダーにログアウトとNewProtoが表示される() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ログアウト")))
                .andExpect(content().string(containsString("New Proto")));
    }
}