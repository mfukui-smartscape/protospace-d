package in.tech_camp.proto_space.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.form.CommentForm;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.repository.CommentRepository;
import in.tech_camp.proto_space.repository.PrototypeRepository;
import in.tech_camp.proto_space.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/prototypes")
public class PrototypeController {

    private final PrototypeRepository prototypeMapper;
    private final UserRepository userMapper;
    private final CommentRepository commentMapper;

    public PrototypeController(PrototypeRepository prototypeMapper,
                                UserRepository userMapper,
                                CommentRepository commentMapper) {
        this.prototypeMapper = prototypeMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
    }

    private boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private User currentUser(Authentication authentication) {
        if (!isLoggedIn(authentication)) {
            return null;
        }
        return userMapper.findByEmail(authentication.getName());
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model, Authentication authentication) {
        Prototype prototype = prototypeMapper.findById(id);
        if (prototype == null) {
            return "redirect:/";
        }

        User loginUser = currentUser(authentication);
        boolean isOwner = loginUser != null && loginUser.getId().equals(prototype.getUserId());

        model.addAttribute("prototype", prototype);
        model.addAttribute("comments", commentMapper.findByPrototypeId(id));
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isLoggedIn", loginUser != null);
        model.addAttribute("commentForm", new CommentForm());

        return "prototypes/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        Prototype prototype = prototypeMapper.findById(id);
        if (prototype == null) {
            return "redirect:/";
        }

        User loginUser = currentUser(authentication);
        if (loginUser == null || !loginUser.getId().equals(prototype.getUserId())) {
            return "redirect:/prototypes/" + id;
        }

        PrototypeForm form = new PrototypeForm();
        form.setName(prototype.getName());
        form.setCatchCopy(prototype.getCatchCopy());
        form.setConcept(prototype.getConcept());

        model.addAttribute("prototypeForm", form);
        model.addAttribute("prototypeId", id);

        return "prototypes/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("prototypeForm") PrototypeForm form,
                          BindingResult bindingResult,
                          Model model,
                          Authentication authentication) {

        Prototype prototype = prototypeMapper.findById(id);
        if (prototype == null) {
            return "redirect:/";
        }

        User loginUser = currentUser(authentication);
        if (loginUser == null || !loginUser.getId().equals(prototype.getUserId())) {
            return "redirect:/prototypes/" + id;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("prototypeId", id);
            return "prototypes/edit";
        }

        prototype.setName(form.getName());
        prototype.setCatchCopy(form.getCatchCopy());
        prototype.setConcept(form.getConcept());
        prototypeMapper.update(prototype);

        return "redirect:/prototypes/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication authentication) {
        Prototype prototype = prototypeMapper.findById(id);
        if (prototype == null) {
            return "redirect:/";
        }

        User loginUser = currentUser(authentication);
        if (loginUser == null || !loginUser.getId().equals(prototype.getUserId())) {
            return "redirect:/prototypes/" + id;
        }

        prototypeMapper.delete(id);
        return "redirect:/";
    }
}
