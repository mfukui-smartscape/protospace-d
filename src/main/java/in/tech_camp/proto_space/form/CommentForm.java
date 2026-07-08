package in.tech_camp.proto_space.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentForm {

    @NotBlank
    private String content;
}
