package in.tech_camp.proto_space.system;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import in.tech_camp.proto_space.entity.Prototype;

public class PrototypeEditSuccessIntegrationTest
        extends AbstractPrototypeIntegrationTest {

    @Test
    @WithMockUser
    void プロトタイプを編集できる()
            throws Exception {

        List<Prototype> beforeList =
                prototypeMapper.findAll();

        int beforeCount =
                beforeList.size();

        MockMultipartFile image =
                new MockMultipartFile(
                        "image",
                        "edited.png",
                        "image/png",
                        "edited image".getBytes());

        mockMvc.perform(
                multipart("/prototypes/{id}",
                        prototype1.getId())
                        .file(image)
                        .param("name",
                                "編集後の名称")
                        .param("catchCopy",
                                "編集後のキャッチコピー")
                        .param("concept",
                                "編集後のコンセプト")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(
                        redirectedUrl(
                                "/prototypes/"
                                        + prototype1.getId()));

        List<Prototype> afterList =
                prototypeMapper.findAll();

        int afterCount =
                afterList.size();

        assertEquals(beforeCount,
                afterCount);
    }

    @Test
    @WithMockUser
    void 画像を変更しなくても画像は保持される()
            throws Exception {

        String beforeImage =
                prototype1.getImageName();

        mockMvc.perform(
                multipart("/prototypes/{id}",
                        prototype1.getId())
                        .param("name",
                                prototype1.getName())
                        .param("catchCopy",
                                prototype1.getCatchCopy())
                        .param("concept",
                                prototype1.getConcept())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        Prototype updated =
                prototypeMapper.findById(
                        prototype1.getId());

        assertEquals(
                beforeImage,
                updated.getImageName());
    }

    @Test
    void 編集済みプロトタイプが詳細画面に表示される()
            throws Exception {

        mockMvc.perform(
                get("/prototypes/{id}",
                        prototype1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(
                                prototype1.getName())));
    }

    @Test
    @WithMockUser
    void 編集画面に登録済み情報が表示される()
            throws Exception {

        mockMvc.perform(
                get("/prototypes/{id}/edit",
                        prototype1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(
                                prototype1.getName())))
                .andExpect(content().string(
                        containsString(
                                prototype1.getCatchCopy())))
                .andExpect(content().string(
                        containsString(
                                prototype1.getConcept())));
    }
}