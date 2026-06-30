package in.tech_camp.proto_space.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@ActiveProfiles("test")
@SpringBootTest
public class UserFormUnitTest {

    private userForm UserForm;

    private Validator validator;

    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
      userForm = UserFormFactory.createUser();

      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      validator = factory.getValidator();

      bindingResult = Mockito.mock(BindingResult.class);
    }
    @Nested
    class ユーザーを作成できる場合{
      public void emailとpasswordとpasswordconfirmationとnameとroleとpositionが存在すれば登録できる(){
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(0, violations.size());
      }
    }
    @Nested
    class ユーザーを作成できない場合  {
      //メールアドレス
      @Test
      public void emailが空では登録できない(){
        userForm.setEmail("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
        assertEquals(1, violations.size());
        assertEquals("Email can't be blank", violations.iterator().next().getMessage());
      }

      @Test
      public void emailはアットマークを含まないと登録できない() {
        userForm.setEmail("invalidmail");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority2.class);
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
      }


}
