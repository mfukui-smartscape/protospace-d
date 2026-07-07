package in.tech_camp.proto_space.form;

<<<<<<< HEAD
import org.springframework.validation.BindingResult;

import in.tech_camp.proto_space.repository.UserMapper;
import in.tech_camp.proto_space.validation.ValidationPriority1;
import in.tech_camp.proto_space.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
=======
>>>>>>> origin/workspace-d
import lombok.Data;

@Data
public class UserForm {
<<<<<<< HEAD

    @NotBlank(message = "Email can't be blank", groups = ValidationPriority1.class)
    @Email(message = "Email should be valid", groups = ValidationPriority2.class)
    private String email;

    @NotBlank(message = "Password can't be blank", groups = ValidationPriority1.class)
    @Size(min = 6, max = 128, message = "Password should be between 6 and 128 characters", groups = ValidationPriority2.class)
    private String password;

    private String passwordConfirmation;

    @NotBlank(message = "Name can't be blank", groups = ValidationPriority1.class)
    private String name;

    @NotBlank(message = "Profile can't be blank", groups = ValidationPriority1.class)
    private String profile;

    @NotBlank(message = "Affiliation can't be blank", groups = ValidationPriority1.class)
    private String affiliation;

    @NotBlank(message = "Position can't be blank", groups = ValidationPriority1.class)
    private String position;

    /**
     * メールアドレス一意性チェック
     */
    public void validateEmailUnique(UserMapper userMapper, BindingResult bindingResult) {
        if (userMapper.countByEmail(this.email) > 0) {
            bindingResult.rejectValue(
                "email",
                "error.user",
                "Email has already been taken");
            }
        }

    /**
     * パスワード確認チェック
     */
    public void validatePasswordConfirmation(BindingResult bindingResult) {
        if (passwordConfirmation == null || passwordConfirmation.isBlank()) {
            bindingResult.rejectValue(
                "passwordConfirmation",
                "error.user",
                "Password confirmation can't be blank");
                return;
            }
            
        if (!password.equals(passwordConfirmation)) {bindingResult.rejectValue(
                "passwordConfirmation",
                "error.user",
                "Password confirmation doesn't match Password");
            }
        }
=======
  private String email;
  private String password;
  private String name;
  private String profile;
  private String affiliation;
  private String position;
>>>>>>> origin/workspace-d
}
