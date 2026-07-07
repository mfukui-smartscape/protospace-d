package in.tech_camp.proto_space.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrototypeForm {

    @NotBlank
    private String name;

    @NotBlank
    private String catchCopy;

    @NotBlank
    private String concept;
}
