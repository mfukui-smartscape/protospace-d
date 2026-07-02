package in.tech_camp.proto_space.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import in.tech_camp.proto_space.entity.Prototype;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final Prototype prototype;

  @GetMapping("/prototypes/new")
  public String showPrototypeNew(@AuthenticationPrincipal Model model) {
      return "prototypes/new";
  }
  
  
}
