package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.repository.PrototypeMapper;
import in.tech_camp.proto_space.validation.ValidationOrder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;





@Controller
@AllArgsConstructor
public class PrototypeController {

  private final PrototypeMapper prototypeMapper;
  //投稿ページ
  @GetMapping("/")
  public String showPrototype(Model model) {
    model.addAttribute("prototypes",prototypeMapper.findAll());
    return "/index";
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
      prototypeMapper.insert(prototype);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
    }
    

    return "redirect:/";
  }
// 編集ページ
  
@GetMapping("/prototypes/{id}")
    public String showDetail(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "prototype",
                prototypeMapper.findById(id));

        return "prototypes/show";
    }

    @GetMapping("/prototypes/{id}/edit")
    public String showEdit(
            @PathVariable Long id,
            Model model) {

        Prototype prototype =
                prototypeMapper.findById(id);

        PrototypeForm form =
                new PrototypeForm();

        form.setName(prototype.getName());
        form.setCatchCopy(prototype.getCatchCopy());
        form.setConcept(prototype.getConcept());

        model.addAttribute(
                "prototype",
                prototype);

        model.addAttribute(
                "prototypeForm",
                form);

        return "prototypes/edit";
    }

    @PostMapping("/prototypes/{id}")
    public String updatePrototype(

            @PathVariable Long id,

            @Validated(ValidationOrder.class)
            @ModelAttribute("prototypeForm")
            PrototypeForm prototypeForm,

            BindingResult result,
            Model model) {

        Prototype prototype =
                prototypeMapper.findById(id);

        if (result.hasErrors()) {

            model.addAttribute(
                    "prototype",
                    prototype);

            model.addAttribute(
                    "prototypeForm",
                    prototypeForm);

            return "prototypes/edit";
        }

        prototype.setName(
                prototypeForm.getName());

        prototype.setCatchCopy(
                prototypeForm.getCatchCopy());

        prototype.setConcept(
                prototypeForm.getConcept());

        MultipartFile imageFile =
                prototypeForm.getImageName();

        // 画像未選択ならそのまま保持
        if (imageFile != null &&
            !imageFile.isEmpty()) {

            prototype.setImageName(
                    imageFile.getOriginalFilename());
        }

        prototypeMapper.update(prototype);

        return "redirect:/prototypes/" + id;
    }

}
