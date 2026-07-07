
package in.tech_camp.proto_space.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.service.PrototypeService;
import in.tech_camp.proto_space.service.UserService;

@Controller
public class UserDetailController {

    private final UserService userService;
    private final PrototypeService prototypeService;

    public UserDetailController(UserService userService, PrototypeService prototypeService) {
        this.userService = userService;
        this.prototypeService = prototypeService;
    }

    @GetMapping("/users/{id}")
    public String show(@PathVariable Long id, Model model) {

        UserEntity user = userService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("prototypes", prototypeService.findByUserId(id));

        return "user_detail";
    }
}
