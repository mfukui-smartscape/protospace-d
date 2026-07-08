package in.tech_camp.proto_space.factories;

import org.springframework.mock.web.MockMultipartFile;

import com.github.javafaker.Faker;

import in.tech_camp.proto_space.form.PrototypeForm;

public class PrototypeFormFactory {

    private static final Faker faker = new Faker();

    public static PrototypeForm createPrototype() {

        PrototypeForm prototypeForm = new PrototypeForm();

        prototypeForm.setName(faker.app().name());
        prototypeForm.setCatchCopy(faker.lorem().sentence());
        prototypeForm.setConcept(faker.lorem().paragraph());

        MockMultipartFile imageName =
                new MockMultipartFile(
                        "image",
                        "test.jpg",
                        "image/jpeg",
                        "dummy image".getBytes());

        prototypeForm.setImageName(imageName);

        return prototypeForm;
    }
}