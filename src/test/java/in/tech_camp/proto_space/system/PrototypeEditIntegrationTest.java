package in.tech_camp.proto_space.system;

import static in.tech_camp.proto_space.support.LoginSupport.login;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

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
import in.tech_camp.proto_space.entity.Prototype;
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
public class PrototypeEditIntegrationTest {

    private UserForm userForm1;
    private User user1;

    private UserForm userForm2;
    private User user2;

    private PrototypeForm prototypeForm1;
    private Prototype prototype1;

    private PrototypeForm prototypeForm2;
    private Prototype prototype2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PrototypeRepository prototypeRepository;

    @BeforeEach
    public void setup() {

        userForm1 = UserFormFactory.createUser();

        user1 = new User();
        user1.setEmail(userForm1.getEmail());
        user1.setName(userForm1.getName());
        user1.setPassword(userForm1.getPassword());

        userService.createUserWithEncryptedPassword(user1);

        userForm2 = UserFormFactory.createUser();

        user2 = new User();
        user2.setEmail(userForm2.getEmail());
        user2.setName(userForm2.getName());
        user2.setPassword(userForm2.getPassword());

        userService.createUserWithEncryptedPassword(user2);

        prototypeForm1 = PrototypeFormFactory.createPrototype();

        prototype1 = new Prototype();
        prototype1.setName(prototypeForm1.getName());
        prototype1.setCatchCopy(prototypeForm1.getCatchCopy());
        prototype1.setConcept(prototypeForm1.getConcept());
        prototype1.setImageName("prototype1.jpg");
        prototype1.setUserId(user1.getId());

        prototypeRepository.insert(prototype1);

        prototypeForm2 = PrototypeFormFactory.createPrototype();

        prototype2 = new Prototype();
        prototype2.setName(prototypeForm2.getName());
        prototype2.setCatchCopy(prototypeForm2.getCatchCopy());
        prototype2.setConcept(prototypeForm2.getConcept());
        prototype2.setImageName("prototype2.jpg");
        prototype2.setUserId(user2.getId());

        prototypeRepository.insert(prototype2);
    }

    @Nested
    class プロトタイプ編集できる場合 {

        @Test
        public void 自分のプロトタイプ編集ページへ遷移できる()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            mockMvc.perform(
                    get("/prototypes/{id}/edit",
                            prototype1.getId())
                            .session(session))
                    .andExpect(status().isOk())
                    .andExpect(view().name("prototypes/edit"))
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

        @Test
        public void 正常に編集できる()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            List<Prototype> beforeList =
                    prototypeRepository.findAll();

            int beforeCount =
                    beforeList.size();

            mockMvc.perform(
                    multipart(
                            "/prototypes/{id}/update",
                            prototype1.getId())
                            .file(
                                 prototypeForm1.getImage())
                            .param(
                                "name",
                                "編集後の名称")
                            .param(
                                "catchCopy",
                                "編集後のコピー")
                            .param(
                                "concept",
                                "編集後のコンセプト")
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(
                        redirectedUrl(
                            "/prototypes/"
                            + prototype1.getId()));

            List<Prototype> afterList =
                    prototypeRepository.findAll();

            int afterCount =
                    afterList.size();

            assertEquals(
                    beforeCount,
                    afterCount);

            Prototype updated =
                    prototypeRepository
                            .findById(
                                prototype1.getId());

            assertEquals(
                    "編集後の名称",
                    updated.getName());

            assertEquals(
                    "編集後のコピー",
                    updated.getCatchCopy());

            assertEquals(
                    "編集後のコンセプト",
                    updated.getConcept());
        }

        @Test
        public void 画像を変更しなくても画像は消えない()
                throws Exception {

            String beforeImage =
                    prototype1.getImageName();

            MockHttpSession session =
                    login(mockMvc, userForm1);

            mockMvc.perform(
                    multipart(
                        "/prototypes/{id}/update",
                        prototype1.getId())
                            .param(
                                "name",
                                prototype1.getName())
                            .param(
                                "catchCopy",
                                prototype1.getCatchCopy())
                            .param(
                                "concept",
                                prototype1.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection());

            Prototype updated =
                    prototypeRepository
                            .findById(
                                prototype1.getId());

            assertEquals(
                    beforeImage,
                    updated.getImageName());
        }
    }

    @Nested
    class プロトタイプ編集できない場合 {

        @Test
        public void 他人の編集ページには遷移できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            mockMvc.perform(
                    get(
                        "/prototypes/{id}/edit",
                        prototype2.getId())
                            .session(session))
                    .andExpect(
                        status()
                            .is3xxRedirection())
                    .andExpect(
                        redirectedUrl("/"));
        }

        @Test
        public void 未ログインでは編集ページへ遷移できない()
                throws Exception {

            mockMvc.perform(
                    get(
                        "/prototypes/{id}/edit",
                        prototype1.getId()))
                    .andExpect(
                        status()
                            .is3xxRedirection())
                    .andExpect(
                        redirectedUrl("/login"));
        }

        @Test
        public void nameが空では編集できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            mockMvc.perform(
                    multipart(
                        "/prototypes/{id}/update",
                        prototype1.getId())
                            .param("name", "")
                            .param(
                                "catchCopy",
                                prototype1.getCatchCopy())
                            .param(
                                "concept",
                                prototype1.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(
                        view().name(
                            "prototypes/edit"));
        }

        @Test
        public void catchCopyが空では編集できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            mockMvc.perform(
                    multipart(
                        "/prototypes/{id}/update",
                        prototype1.getId())
                            .param(
                                "name",
                                prototype1.getName())
                            .param(
                                "catchCopy",
                                "")
                            .param(
                                "concept",
                                prototype1.getConcept())
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(
                        view().name(
                            "prototypes/edit"));
        }

        @Test
        public void conceptが空では編集できない()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            mockMvc.perform(
                    multipart(
                        "/prototypes/{id}/update",
                        prototype1.getId())
                            .param(
                                "name",
                                prototype1.getName())
                            .param(
                                "catchCopy",
                                prototype1.getCatchCopy())
                            .param(
                                "concept",
                                "")
                            .session(session)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(
                        view().name(
                            "prototypes/edit"));
        }

        @Test
        public void 編集失敗時も入力値が保持される()
                throws Exception {

            MockHttpSession session =
                    login(mockMvc, userForm1);

            MvcResult result =
                    mockMvc.perform(
                            multipart(
                              "/prototypes/{id}/update",
                              prototype1.getId())
                                    .param(
                                        "name",
                                        "編集中の名称")
                                    .param(
                                        "catchCopy",
                                        "")
                                    .param(
                                        "concept",
                                        "編集中の説明")
                                    .session(session)
                                    .with(csrf()))
                            .andExpect(status().isOk())
                            .andExpect(
                                view().name(
                                    "prototypes/edit"))
                            .andReturn();

            String html =
                    result.getResponse()
                          .getContentAsString();

            assertThat(
                    html,
                    containsString(
                        "編集中の名称"));

            assertThat(
                    html,
                    containsString(
                        "編集中の説明"));
        }
    }
}