package in.tech_camp.proto_space.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import in.tech_camp.proto_space.entity.Prototype;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final Prototype prototype;

  @GetMapping("/prototypes/new")
  public String showPrototypeNew( Model model) {
    model.addAttribute("prototype",new Prototype());
    return "prototypes/new";
  }
  
  @PostMapping("/prototypes")
  public String createPrototype(@ModelAttribute("prototype")) {
      
      return entity;
  }
  
}
