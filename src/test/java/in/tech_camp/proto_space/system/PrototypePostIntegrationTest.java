package in.tech_camp.proto_space.system;

import static in.tech_camp.proto_space.support.LoginSupport.login;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import in.tech_camp.proto_space.ProtoSpaceApplication;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.factory.PrototypeFormFactory;
import in.tech_camp.proto_space.factory.UserFormFactory;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.form.UserForm;
import in.tech_camp.proto_space.repository.PrototypeRepository;
import in.tech_camp.proto_space.service.UserService;

@ActiveProfiles("test")
@SpringBootTest(classes = ProtoSpaceApplication.class)
@AutoConfigureMockMvc
public class PrototypePostIntegrationTest {

    private UserForm userForm;
    private User user;

    private PrototypeForm prototypeForm;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PrototypeRepository prototypeRepository;

    @BeforeEach
    public void setup() {

        userForm = UserFormFactory.createUser();

        user = new User();
        user.setEmail(userForm.getEmail());
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());

        userService.createUserWithEncryptedPassword(user);

        prototypeForm = PrototypeFormFactory.createPrototype();
    }

    @Nested
    class プロトタイプ投稿できる場合 {

        @Test
        public void ログインユーザーは投稿できる() throws Exception {

            MockHttpSession session = login(mockMvc, userForm);

            assertNotNull(session);

            mockMvc.perform(
                    get("/prototypes/new")
                            .session(session))
                    .andExpect(status().isOk())
                    .andExpect(view().name("prototypes/new"));

            int beforeCount =
                    prototypeRepository.findAll().size();

            mockMvc.perform(
                    multipart("/prototypes")
                            .file(prototypeForm.getImage())
                            .param("name", prototypeForm.getName())
                            .param("catchCopy", prototypeForm.getCatchCopy())
                            .param("concept", prototypeForm.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"));

            int afterCount =
                    prototypeRepository.findAll().size();

            assertEquals(beforeCount + 1, afterCount);

            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                            containsString(prototypeForm.getName())))
                    .andExpect(content().string(
                            containsString(prototypeForm.getCatchCopy())))
                    .andExpect(content().string(
                            containsString(userForm.getName())));
        }

        @Test
        public void 投稿した画像が表示される() throws Exception {

            MockHttpSession session = login(mockMvc, userForm);

            mockMvc.perform(
                    multipart("/prototypes")
                            .file(prototypeForm.getImage())
                            .param("name", prototypeForm.getName())
                            .param("catchCopy", prototypeForm.getCatchCopy())
                            .param("concept", prototypeForm.getConcept())
                            .session(session)
                            .with(csrf()));

            MvcResult result =
                    mockMvc.perform(get("/"))
                            .andReturn();

            String html =
                    result.getResponse()
                            .getContentAsString();

            Document document =
                    Jsoup.parse(html);

            Element imageElement =
                    document.selectFirst("img");

            assertNotNull(imageElement);

            String src =
                    imageElement.attr("src");

            assertThat(src, not(emptyString()));
        }
    }

    @Nested
    class プロトタイプ投稿できない場合 {

        @Test
        public void 未ログインユーザーは投稿ページへ遷移できない()
                throws Exception {

            mockMvc.perform(get("/prototypes/new"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"));
        }

        @Test
        public void nameが空では投稿できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm);

            int beforeCount =
                    prototypeRepository.findAll().size();

            mockMvc.perform(
                    multipart("/prototypes")
                            .file(prototypeForm.getImage())
                            .param("name", "")
                            .param("catchCopy",
                                    prototypeForm.getCatchCopy())
                            .param("concept",
                                    prototypeForm.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("prototypes/new"));

            int afterCount =
                    prototypeRepository.findAll().size();

            assertEquals(beforeCount, afterCount);
        }

        @Test
        public void catchCopyが空では投稿できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm);

            mockMvc.perform(
                    multipart("/prototypes")
                            .file(prototypeForm.getImage())
                            .param("name",
                                    prototypeForm.getName())
                            .param("catchCopy", "")
                            .param("concept",
                                    prototypeForm.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("prototypes/new"));
        }

        @Test
        public void conceptが空では投稿できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm);

            mockMvc.perform(
                    multipart("/prototypes")
                            .file(prototypeForm.getImage())
                            .param("name",
                                    prototypeForm.getName())
                            .param("catchCopy",
                                    prototypeForm.getCatchCopy())
                            .param("concept", "")
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("prototypes/new"));
        }

        @Test
        public void imageが空では投稿できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm);

            mockMvc.perform(
                    multipart("/prototypes")
                            .param("name",
                                    prototypeForm.getName())
                            .param("catchCopy",
                                    prototypeForm.getCatchCopy())
                            .param("concept",
                                    prototypeForm.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("prototypes/new"));
        }

        @Test
        public void バリデーションエラーでも入力値が保持される()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm);

            MvcResult result =
                    mockMvc.perform(
                            multipart("/prototypes")
                                    .param("name",
                                            "テストプロトタイプ")
                                    .param("catchCopy", "")
                                    .param("concept",
                                            "テストコンセプト")
                                    .session(session)
                                    .with(csrf()))
                            .andExpect(status().isOk())
                            .andExpect(view().name("prototypes/new"))
                            .andReturn();

            String html =
                    result.getResponse()
                            .getContentAsString();

            assertThat(
                    html,
                    containsString("テストプロトタイプ"));

            assertThat(
                    html,
                    containsString("テストコンセプト"));
        }
    }
}