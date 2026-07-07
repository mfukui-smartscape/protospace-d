package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.form.UserForm;
import in.tech_camp.proto_space.repository.UserMapper;
import in.tech_camp.proto_space.service.PrototypeService;
import in.tech_camp.proto_space.service.UserService;
import in.tech_camp.proto_space.validation.ValidationOrder;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;
  private final PrototypeService prototypeService;

  @GetMapping("/users/new")
  public String newUser(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/new";
  }

    @PostMapping("/users")
    public String createUser(@Validated(ValidationOrder.class) @ModelAttribute UserForm userForm, BindingResult bindingResult, Model model) {
        userForm.validateEmailUnique(userMapper, bindingResult);
        userForm.validatePasswordConfirmation(bindingResult);
        if (bindingResult.hasErrors()) {
                model.addAttribute("userForm", userForm);
                return "users/new";
        }
        UserEntity user = new UserEntity();
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setName(userForm.getName());
        user.setProfile(userForm.getProfile());
        user.setAffiliation(userForm.getAffiliation());
        user.setPosition(userForm.getPosition());
        userService.createUserWithEncryptedPassword(user);
        return "redirect:/login";
    }

    @GetMapping("/users/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        UserEntity user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("prototypes", prototypeService.findByUserId(id));
        // 2. UserController.showDetail の return を変更
        return "users/detail";   // user_detail → users/detail
    }

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }
}