package in.tech_camp.proto_space.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.form.PrototypeForm;
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
  public String createPrototype(@Validated@ModelAttribute("prototypeForm") PrototypeForm prototypeForm,BindingResult result) {

    if (result.hasErrors()) {
        return "prototypes/new";
    }

    Prototype prototype = new Prototype();

    prototype.setName(prototypeForm.getName());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());
    prototype.setConcept(prototypeForm.getConcept());

    prototypeRepository.save(prototype);

    return "redirect:/";
}

  
}
