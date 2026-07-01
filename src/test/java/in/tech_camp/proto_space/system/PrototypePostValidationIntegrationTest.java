package in.tech_camp.proto_space.system;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class PrototypePostValidationIntegrationTest
        extends AbstractPrototypeIntegrationTest {

    @Test
    @WithMockUser
    void name未入力では投稿できない()
            throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "image",
                        "test.png",
                        "image/png",
                        "dummy".getBytes());

        mockMvc.perform(
                multipart("/prototypes")
                        .file(file)
                        .param("name", "")
                        .param("catchCopy", "コピー")
                        .param("concept", "コンセプト")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("prototypes/new"));
    }

    @Test
    @WithMockUser
    void catchCopy未入力では投稿できない()
            throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "image",
                        "test.png",
                        "image/png",
                        "dummy".getBytes());

        mockMvc.perform(
                multipart("/prototypes")
                        .file(file)
                        .param("name", "名称")
                        .param("catchCopy", "")
                        .param("concept", "コンセプト")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("prototypes/new"));
    }

    @Test
    @WithMockUser
    void concept未入力では投稿できない()
            throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "image",
                        "test.png",
                        "image/png",
                        "dummy".getBytes());

        mockMvc.perform(
                multipart("/prototypes")
                        .file(file)
                        .param("name", "名称")
                        .param("catchCopy", "コピー")
                        .param("concept", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("prototypes/new"));
    }
}