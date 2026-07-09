package in.tech_camp.proto_space.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.entity.UserEntity;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.repository.CommentMapper;
import in.tech_camp.proto_space.service.PrototypeService;
import in.tech_camp.proto_space.service.UserService;
import in.tech_camp.proto_space.validation.ValidationOrder;
import in.tech_camp.proto_space.validation.ValidationPriority1;

@Controller
public class PrototypeController {

    private final PrototypeService prototypeService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public PrototypeController(PrototypeService prototypeService, UserService userService,
                               CommentMapper commentMapper) {
        this.prototypeService = prototypeService;
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    // トップページ（一覧）
    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        model.addAttribute("prototypes", prototypeService.findAll());
        if (authentication != null) {
            UserEntity user = userService.findByEmail(authentication.getName());
            if (user != null) {
                model.addAttribute("userName", user.getName());
            }
        }
        return "index";
    }

    // 投稿フォーム表示
    @GetMapping("/prototypes/new")
    public String showNew(Model model) {
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "prototypes/new";
    }

    // 詳細表示（誰でも見れる。本人だけ編集/削除リンクを出す）
    @GetMapping("/prototypes/{id}")
    public String showDetail(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        PrototypeEntity prototype = prototypeService.findById(id);
        model.addAttribute("prototype", prototype);
        model.addAttribute("comments", commentMapper.findByPrototypeId(id));

        boolean isOwner = false;
        boolean isLoggedIn = false;
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            isLoggedIn = true;
            UserEntity loginUser = userService.findByEmail(authentication.getName());
            if (loginUser != null && loginUser.getId().equals(prototype.getUserId())) {
                isOwner = true;
            }
        }
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "prototypes/detail";
    }

    // 編集フォーム表示（本人のみ）
    @GetMapping("/prototypes/{id}/edit")
    public Object showEdit(@PathVariable Long id, Authentication authentication, Model model) {
        PrototypeEntity prototype = prototypeService.findById(id);
        UserEntity loginUser = userService.findByEmail(authentication.getName());

        if (!prototype.getUserId().equals(loginUser.getId())) {
            return new RedirectView("/");
        }

        PrototypeForm form = new PrototypeForm();
        form.setName(prototype.getName());
        form.setCatchCopy(prototype.getCatchCopy());
        form.setConcept(prototype.getConcept());

        model.addAttribute("prototype", prototype);
        model.addAttribute("prototypeForm", form);
        return "prototypes/edit";
    }

    // 投稿
@PostMapping("/prototypes")
public String create(
        @Validated(ValidationOrder.class) @ModelAttribute("prototypeForm") PrototypeForm prototypeForm,
        BindingResult result,
        Authentication authentication) {

    if (result.hasErrors()) {
        return "prototypes/new";
    }

    UserEntity loginUser = userService.findByEmail(authentication.getName());

    PrototypeEntity prototype = new PrototypeEntity();
    prototype.setName(prototypeForm.getName());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setUserId(loginUser.getId());

    MultipartFile imageFile = prototypeForm.getImageName();
    if (imageFile != null && !imageFile.isEmpty()) {
        String originalFilename = imageFile.getOriginalFilename();
        String savedFilename = UUID.randomUUID() + "_" + originalFilename;

        try {
            // プロジェクトルートを基準にした絶対パスに変更
            Path uploadDir = Paths.get(System.getProperty("user.dir"),
                    "uploads", "images");
            Files.createDirectories(uploadDir); // フォルダが無ければ作成

            Path destination = uploadDir.resolve(savedFilename);
            imageFile.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("画像の保存に失敗しました", e);
        }

        prototype.setImageName(savedFilename);
    }

    prototypeService.save(prototype);
    return "redirect:/";
}

    // 更新（本人のみ）
    @PostMapping("/prototypes/{id}")
public String update(
        @PathVariable Long id,
        @Validated(ValidationPriority1.class) @ModelAttribute("prototypeForm") PrototypeForm prototypeForm,
        BindingResult result,
        Authentication authentication,
        Model model) {

    PrototypeEntity prototype = prototypeService.findById(id);
    if (prototype == null) {
        return "redirect:/";
    }

    UserEntity loginUser = (authentication != null)
            ? userService.findByEmail(authentication.getName()) : null;
    if (loginUser == null || !loginUser.getId().equals(prototype.getUserId())) {
        return "redirect:/prototypes/" + id;
    }

    if (result.hasErrors()) {
        model.addAttribute("prototype", prototype);
        return "prototypes/edit";
    }

    prototype.setName(prototypeForm.getName());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());
    prototype.setConcept(prototypeForm.getConcept());

    MultipartFile imageFile = prototypeForm.getImageName();
    if (imageFile != null && !imageFile.isEmpty()) {
        String originalFilename = imageFile.getOriginalFilename();
        String savedFilename = UUID.randomUUID() + "_" + originalFilename;

        try {
            Path uploadDir = Paths.get(System.getProperty("user.dir"),
                    "uploads", "images");
            Files.createDirectories(uploadDir);

            Path destination = uploadDir.resolve(savedFilename);
            imageFile.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("画像の保存に失敗しました", e);
        }

        prototype.setImageName(savedFilename);
    }

    prototypeService.update(prototype);
    return "redirect:/prototypes/" + id;
}

    // 削除（本人のみ）
    @PostMapping("/prototypes/{id}/delete")
    public String delete(
            @PathVariable Long id,
            Authentication authentication) {

        PrototypeEntity prototype = prototypeService.findById(id);
        if (prototype == null) {
            return "redirect:/";
        }

        UserEntity loginUser = (authentication != null)
                ? userService.findByEmail(authentication.getName()) : null;
        if (loginUser == null || !loginUser.getId().equals(prototype.getUserId())) {
            return "redirect:/prototypes/" + id;
        }

        prototypeService.delete(id);
        return "redirect:/";
    }
}