package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.proto_space.service.PrototypeService;

@Controller
public class TopPageController {
  private final PrototypeService prototypeService;
  public TopPageController(PrototypeService prototypeService) {
    this.prototypeService = prototypeService;
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("prototype", prototypeService.findAll());

    return "index";
  }
}
