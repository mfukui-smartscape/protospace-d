package in.tech_camp.proto_space.form;

<<<<<<< HEAD
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.proto_space.validation.ValidationPriority1;
import in.tech_camp.proto_space.validation.ValidationPriority3;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
=======
>>>>>>> origin/workspace-d
import lombok.Data;

@Data
public class PrototypeForm {
<<<<<<< HEAD
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

=======
  private String name;
  private String catchCopy;
  private String concept;
  private String imageName;
>>>>>>> origin/workspace-d
}
