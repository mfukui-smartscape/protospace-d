package in.tech_camp.proto_space.system;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class PrototypeEditValidationIntegrationTest
        extends AbstractPrototypeIntegrationTest {

    @Test
    @WithMockUser
    void name未入力では編集できない()
            throws Exception {

        mockMvc.perform(
                multipart("/prototypes/{id}",
                        prototype1.getId())
                        .param("name", "")
                        .param("catchCopy",
                                prototype1.getCatchCopy())
                        .param("concept",
                                prototype1.getConcept())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("prototypes/edit"));
    }

    @Test
    @WithMockUser
    void catchCopy未入力では編集できない()
            throws Exception {

        mockMvc.perform(
                multipart("/prototypes/{id}",
                        prototype1.getId())
                        .param("name",
                                prototype1.getName())
                        .param("catchCopy", "")
                        .param("concept",
                                prototype1.getConcept())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("prototypes/edit"));
    }

    @Test
    @WithMockUser
    void concept未入力では編集できない()
            throws Exception {

        mockMvc.perform(
                multipart("/prototypes/{id}",
                        prototype1.getId())
                        .param("name",
                                prototype1.getName())
                        .param("catchCopy",
                                prototype1.getCatchCopy())
                        .param("concept", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("prototypes/edit"));
    }

    @Test
    @WithMockUser
    void 編集失敗時も入力値が保持される()
            throws Exception {

        MvcResult result =
                mockMvc.perform(
                        multipart("/prototypes/{id}",
                                prototype1.getId())
                                .param("name",
                                        "編集中の名称")
                                .param("catchCopy", "")
                                .param("concept",
                                        "編集中のコンセプト")
                                .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(view().name("prototypes/edit"))
                        .andReturn();

        String html =
                result.getResponse()
                        .getContentAsString();

        assertThat(html,
                containsString("編集中の名称"));

        assertThat(html,
                containsString("編集中のコンセプト"));
    }
}