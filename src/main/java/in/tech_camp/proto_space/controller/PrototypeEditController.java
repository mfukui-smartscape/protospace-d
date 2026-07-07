package in.tech_camp.proto_space.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.service.PrototypeService;
import in.tech_camp.proto_space.service.UserService;

@Controller
public class PrototypeEditController {

  private final PrototypeService prototypeService;
  private final UserService userService;

  public PrototypeEditController(PrototypeService prototypeService, UserService userService) {
    this.prototypeService = prototypeService;
    this.userService = userService;
  }

  @GetMapping("/prototypes/{id}/edit")
  public Object show(@PathVariable Long id, Authentication authentication, Model model) {
      PrototypeEntity prototype = prototypeService.findById(id);
      UserEntity loginUser = userService.findByEmail(authentication.getName());

      if (!prototype.getUserId().equals(loginUser.getId())) {
          return new RedirectView("/");
      }

      PrototypeForm form = new PrototypeForm();
      form.setName(prototype.getName());
      form.setCatchCopy(prototype.getCatchCopy());
      form.setConcept(prototype.getConcept());

      model.addAttribute("prototype", prototype);
      model.addAttribute("prototypeForm", form);
      return "prototypes/edit";
  }
}