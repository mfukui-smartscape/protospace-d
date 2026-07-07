package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.proto_space.form.PrototypeForm;

@Controller
public class PrototypeNewController {

  @GetMapping("/prototypes/new")
  public String show(Model model) {
    model.addAttribute("prototypeForm", new PrototypeForm());
    return "prototypes/new";
  }
}