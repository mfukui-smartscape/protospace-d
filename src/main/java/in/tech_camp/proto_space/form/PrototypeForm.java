package in.tech_camp.proto_space.form;

import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.proto_space.validation.ValidationPriority1;
import in.tech_camp.proto_space.validation.ValidationPriority3;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrototypeForm {
  @NotBlank(message = "Name can't be blank",groups = ValidationPriority1.class)
  private String name;
  @NotBlank(message = "Catch copy can't be blank", groups = ValidationPriority1.class)
  private String catchCopy;
  @NotBlank(message = "Concept can't be blank", groups = ValidationPriority1.class)
  private String concept;
  @NotNull(message = "Image can't be blank", groups = ValidationPriority3.class)
  private MultipartFile imageName;

  
 public MultipartFile getImage() {
      return imageName;
  }

}