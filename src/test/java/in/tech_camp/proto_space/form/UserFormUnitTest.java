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

import in.tech_camp.proto_space.factories.UserFormFactory;
import in.tech_camp.proto_space.validation.ValidationPriority1;
import in.tech_camp.proto_space.validation.ValidationPriority2;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@ActiveProfiles("test")
@SpringBootTest
public class UserFormUnitTest {

    private UserForm userForm;

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
      @Test
      public void emailとpasswordとpasswordconfirmationとnameとroleとpositionが存在すれば登録できる(){
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(0, violations.size());
      }
    }
    @Nested
    class ユーザーを作成できない場合  {
      //メールアドレス
      @Test
      public void emailが空では登録できない(){//ユーザーの新規登録には、メールアドレスが必須であること
        userForm.setEmail("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);

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

      @Test
      public void emailが重複している場合は登録できない() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        // 「このメールは既に存在する」と仮定
        Mockito.when(userRepository.existsByEmail(userForm.getEmail()))
           .thenReturn(true);

        // バリデーション実行
        userForm.validateEmailUnique(userRepository, bindingResult);

        // エラーが追加されたか確認
        verify(bindingResult).rejectValue(
          "email",
          "error.user",
          "Email has already been taken"
        );
      }

      //パスワード

      @Test
      public void passwordが空では登録できない() {
        userForm.setPassword("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Password can't be blank", violations.iterator().next().getMessage());
      }

      @Test
      public void passwordが5文字以下では登録できない() {
        userForm.setPassword("setUp");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority2.class);
        assertEquals(1, violations.size());
        assertEquals("Password should be between 6 and 128 characters", violations.iterator().next().getMessage());
      }

      @Test
      public void passwordとpasswordConfirmationが不一致では登録できない() {
        userForm.setPasswordConfirmation("differentPassword");
        userForm.validatePasswordConfirmation(bindingResult);
        verify(bindingResult).rejectValue("passwordConfirmation","error.user","Password confirmation doesn't match Password");
      }

      
      
      @Test
      public void passwordConfirmationが空では登録できない() {
        userForm.setPasswordConfirmation("");
        userForm.validatePasswordConfirmation(bindingResult);

        verify(bindingResult).rejectValue(
          "passwordConfirmation",
          "error.user",
          "Password confirmation can't be blank"
        );
      }


      
      @Test
      public void nameが空では登録できない () {
        userForm.setName("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Name can't be blank", violations.iterator().next().getMessage());
      }

      @Test
      public void profileが空では登録できない () {
        userForm.setProfile("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Profile can't be blank", violations.iterator().next().getMessage());
      }

      @Test//所属
      public void roleが空で登録できない(){
        userForm.setRole("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Role can't be blank", violations.iterator().next().getMessage());
      }
      @Test//役職
      public void positionが空では登録できない(){
        userForm.setPosition("");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm,ValidationPriority1.class);
        assertEquals(1, violations.size());
        assertEquals("Position can't be blank", violations.iterator().next().getMessage());
      }
    }
}
