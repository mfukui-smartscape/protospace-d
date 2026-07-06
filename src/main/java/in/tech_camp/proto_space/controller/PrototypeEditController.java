package in.tech_camp.proto_space.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
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
  public Object show(@PathVariable Long id, Authentication authentication) {
    Prototype prototype = prototypeService.findById(id);
    User loginUser = userService.findByEmail(authentication.getName());

    if (!prototype.getUserId().equals(loginUser.getId())) {
      return new RedirectView("/");
    }

    return "prototype_edit";
  }
}
