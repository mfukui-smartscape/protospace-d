package in.tech_camp.proto_space.system;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MvcResult;
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
                        "imageName",
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
                        "imageName",
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
                        "imageName",
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

    @Test
    @WithMockUser(username = "user1@test.com")
    void 投稿失敗時も入力値が保持される()
            throws Exception {

        MvcResult result =
                mockMvc.perform(
                        multipart("/prototypes")
                                .param("name",
                                        "投稿中の名称")
                                .param("catchCopy", "")
                                .param("concept",
                                        "投稿中のコンセプト")
                                .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(view().name("prototypes/new"))
                        .andReturn();

        String html =
                result.getResponse()
                        .getContentAsString();

        assertThat(html,
                containsString("投稿中の名称"));

        assertThat(html,
                containsString("投稿中のコンセプト"));
    }
}