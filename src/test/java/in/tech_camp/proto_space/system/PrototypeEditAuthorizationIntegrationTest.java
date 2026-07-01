package in.tech_camp.proto_space.system;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PrototypeEditAuthorizationIntegrationTest
        extends AbstractPrototypeIntegrationTest {

    @Test
    @WithMockUser
    void 自分のプロトタイプ編集ページへ遷移できる()
            throws Exception {

        mockMvc.perform(
                get("/prototypes/{id}/edit",
                        prototype1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 他人のプロトタイプ編集ページへ遷移できない()
            throws Exception {

        mockMvc.perform(
                get("/prototypes/{id}/edit",
                        prototype2.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void 未ログインでは編集ページへ遷移できない()
            throws Exception {

        mockMvc.perform(
                get("/prototypes/{id}/edit",
                        prototype1.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("**/login"));
    }
}