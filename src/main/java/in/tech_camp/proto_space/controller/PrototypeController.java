package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.repository.PrototypeRepository;
import in.tech_camp.proto_space.validation.ValidationOrder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;





@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;

  @GetMapping("/")
  public String showPrototype(Model model) {
    model.addAttribute("prototypes",prototypeRepository.findAll());
    return "index";
  }
  
  @GetMapping("/prototypes/new")
  public String showPrototypeNew( Model model) {
    model.addAttribute("prototypeForm",new PrototypeForm());
    return "prototypes/new";
  }
  
  
  @PostMapping("/prototypes")
  public String createPrototype(@Validated(ValidationOrder.class)@ModelAttribute("prototypeForm") PrototypeForm prototypeForm,BindingResult result) {

    if (result.hasErrors()) {
        return "prototypes/new";
    }

    Prototype prototype = new Prototype();

    
    MultipartFile imageFile = prototypeForm.getImageName();

    if (imageFile != null && !imageFile.isEmpty()) {

      try {
        String fileName = imageFile.getOriginalFilename();

        prototype.setImageName(fileName);

      } catch (Exception e) {
        System.out.println("エラー：" + e);
        return "prototypes/new";
      }
    }

    prototype.setName(prototypeForm.getName());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());
    prototype.setConcept(prototypeForm.getConcept());
    try{
      prototypeRepository.insert(prototype);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
    }
    

    return "redirect:/";
  }
}
