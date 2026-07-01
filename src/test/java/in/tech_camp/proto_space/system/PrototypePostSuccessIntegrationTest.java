package in.tech_camp.proto_space.system;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PrototypePostSuccessIntegrationTest
        extends AbstractPrototypeIntegrationTest {

    @Test
    @WithMockUser
    void プロトタイプ投稿できる()
            throws Exception {

        int beforeCount =
                prototypeMapper.findAll().size();

        MockMultipartFile file =
                new MockMultipartFile(
                        "image",
                        "test.png",
                        "image/png",
                        "dummy".getBytes());

        mockMvc.perform(
                multipart("/prototypes")
                        .file(file)
                        .param("name", "投稿テスト")
                        .param("catchCopy", "キャッチコピー")
                        .param("concept", "コンセプト")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        int afterCount =
                prototypeMapper.findAll().size();

        assertEquals(
                beforeCount + 1,
                afterCount);
    }

    @Test
    void 投稿済みプロトタイプがトップページに表示される()
            throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(content().string(
                        containsString(prototype1.getName())));
    }
}