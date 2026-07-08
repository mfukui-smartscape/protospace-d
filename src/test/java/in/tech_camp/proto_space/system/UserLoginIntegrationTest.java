package in.tech_camp.proto_space.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.factories.UserFormFactory;
import in.tech_camp.proto_space.form.UserForm;
import in.tech_camp.proto_space.service.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private UserForm userForm;

    @BeforeEach
    void setUp() {

        userForm = UserFormFactory.createUser();

        UserEntity user = new UserEntity();
        user.setEmail(userForm.getEmail());
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());
        user.setProfile(userForm.getProfile());
        user.setAffiliation(userForm.getAffiliation());
        user.setPosition(userForm.getPosition());

        userService.createUserWithEncryptedPassword(user);
    }

    @Nested
    class ログインできる場合 {

        @Test
        void 保存済みユーザー情報と一致すればログインできる()
                throws Exception {

            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk());

            MvcResult loginResult =
                    mockMvc.perform(
                            post("/login")
                                    .contentType(
                                            MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("email",
                                            userForm.getEmail())
                                    .param("password",
                                            userForm.getPassword())
                                    .with(csrf()))
                            .andExpect(status().isFound())
                            .andExpect(redirectedUrl("/"))
                            .andReturn();

            MockHttpSession session =
                    (MockHttpSession) loginResult
                            .getRequest()
                            .getSession();

            mockMvc.perform(
                    get("/")
                            .session(session))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class ログインできない場合 {

        @Test
        void メールアドレスが不正ではログインできない()
                throws Exception {

            mockMvc.perform(
                    post("/login")
                            .contentType(
                                    MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email",
                                    "invalid@test.com")
                            .param("password",
                                    userForm.getPassword())
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(
                            redirectedUrl("/login?error"));
        }

        @Test
        void パスワードが不正ではログインできない()
                throws Exception {

            mockMvc.perform(
                    post("/login")
                            .contentType(
                                    MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email",
                                    userForm.getEmail())
                            .param("password",
                                    "wrong-password")
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(
                            redirectedUrl("/login?error"));
        }

        @Test
        void emailが空ではログインできない()
                throws Exception {

            mockMvc.perform(
                    post("/login")
                            .contentType(
                                    MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email", "")
                            .param("password",
                                    userForm.getPassword())
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(
                            redirectedUrl("/login?error"));
        }

        @Test
        void passwordが空ではログインできない()
                throws Exception {

            mockMvc.perform(
                    post("/login")
                            .contentType(
                                    MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email",
                                    userForm.getEmail())
                            .param("password", "")
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(
                            redirectedUrl("/login?error"));
        }
    }
}