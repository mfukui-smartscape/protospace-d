package in.tech_camp.proto_space.system;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PrototypePostAuthorizationIntegrationTest
        extends AbstractPrototypeIntegrationTest {

    @Test
    @WithMockUser
    void ログインユーザーは投稿ページへ遷移できる()
            throws Exception {

        mockMvc.perform(get("/prototypes/new"))
                .andExpect(status().isOk());
    }

    @Test
    void 未ログインユーザーはログイン画面へ遷移する()
            throws Exception {

        mockMvc.perform(get("/prototypes/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("**/login"));
    }
}