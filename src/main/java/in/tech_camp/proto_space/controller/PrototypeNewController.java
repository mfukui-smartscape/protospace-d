package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrototypeNewController {
  @GetMapping("/prototypes/new")
  public String show() {
    return "prototype/new";
  }
}
