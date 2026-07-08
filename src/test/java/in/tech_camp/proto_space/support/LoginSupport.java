
package in.tech_camp.proto_space.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import in.tech_camp.proto_space.form.UserForm;

public class LoginSupport {

    public static MockHttpSession login(
            MockMvc mockMvc,
            UserForm userForm) throws Exception {

        MvcResult result =
                mockMvc.perform(
                        post("/login")
                                .param("email", userForm.getEmail())
                                .param("password", userForm.getPassword()))
                        .andReturn();

        return (MockHttpSession)
                result.getRequest().getSession(false);
    }
}
