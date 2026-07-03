package in.tech_camp.proto_space.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.tech_camp.proto_space.entity.Comment;
import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.form.CommentForm;
import in.tech_camp.proto_space.repository.CommentRepository;
import in.tech_camp.proto_space.repository.PrototypeRepository;
import in.tech_camp.proto_space.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/prototypes/{prototypeId}/comments")
public class CommentController {

    private final CommentRepository commentMapper;
    private final PrototypeRepository prototypeMapper;
    private final UserRepository userMapper;

    public CommentController(CommentRepository commentMapper,
                              PrototypeRepository prototypeMapper,
                              UserRepository userMapper) {
        this.commentMapper = commentMapper;
        this.prototypeMapper = prototypeMapper;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String create(@PathVariable Long prototypeId,
                          @Valid @ModelAttribute("commentForm") CommentForm form,
                          BindingResult bindingResult,
                          Model model,
                          Authentication authentication) {

        Prototype prototype = prototypeMapper.findById(prototypeId);
        if (prototype == null) {
            return "redirect:/";
        }

        User loginUser = userMapper.findByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            boolean isOwner = loginUser != null && loginUser.getId().equals(prototype.getUserId());
            List<Comment> comments = commentMapper.findByPrototypeId(prototypeId);

            model.addAttribute("prototype", prototype);
            model.addAttribute("comments", comments);
            model.addAttribute("isOwner", isOwner);
            model.addAttribute("isLoggedIn", true);

            return "prototypes/detail";
        }

        Comment comment = new Comment();
        comment.setContent(form.getContent());
        comment.setPrototypeId(prototypeId);
        comment.setUserId(loginUser.getId());
        commentMapper.insert(comment);

        return "redirect:/prototypes/" + prototypeId;
    }
}
