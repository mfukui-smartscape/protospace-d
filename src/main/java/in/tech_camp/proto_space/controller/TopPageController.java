package in.tech_camp.proto_space.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.proto_space.entity.User;
import in.tech_camp.proto_space.service.PrototypeService;
import in.tech_camp.proto_space.service.UserService;

@Controller
public class TopPageController {

    private final PrototypeService prototypeService;
    private final UserService userService;

    public TopPageController(PrototypeService prototypeService, UserService userService) {
        this.prototypeService = prototypeService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {

        model.addAttribute("prototypes", prototypeService.findAll());

        if (authentication != null) {

            User user = userService.findByEmail(authentication.getName());

            if (user != null) {
                model.addAttribute("userName", user.getName());
            }
        }

        return "index";
    }
}