package in.tech_camp.proto_space.form;

import in.tech_camp.proto_space.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrototypeForm {
  @NotBlank(message = "Name can't be blank",groups = ValidationPriority1.class)
  private String name;
  @NotBlank(message = "Catch copy can't be blank", groups = ValidationPriority1.class)
  private String catchCopy;
  @NotBlank(message = "Concept can't be blank", groups = ValidationPriority1.class)
  private String concept;
  @NotBlank(message = "Image can't be blank", groups = ValidationPriority1.class)
  private String imageName;
}