package in.tech_camp.proto_space.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.proto_space.factory.PrototypeFormFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class PrototypeFormUnitTest {

    private PrototypeForm prototypeForm;
    private Validator validator;

    @BeforeEach
    public void setUp() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();

        prototypeForm =
                PrototypeFormFactory.createPrototype();
    }

    @Nested
    class プロトタイプ投稿できる場合 {

        @Test
        public void nameとcatchCopyとconceptとimageが存在すれば投稿できる() {

            Set<ConstraintViolation<PrototypeForm>> violations =
                    validator.validate(prototypeForm);

            assertEquals(0, violations.size());
        }
    }

    @Nested
    class プロトタイプ投稿できない場合 {

        @Test
        public void nameが空の場合バリデーションエラーが発生する() {

            prototypeForm.setName("");

            Set<ConstraintViolation<PrototypeForm>> violations =
                    validator.validate(prototypeForm);

            assertEquals(1, violations.size());

            assertEquals(
                    "Name can't be blank",
                    violations.iterator().next().getMessage());
        }

        @Test
        public void catchCopyが空の場合バリデーションエラーが発生する() {

            prototypeForm.setCatchCopy("");

            Set<ConstraintViolation<PrototypeForm>> violations =
                    validator.validate(prototypeForm);

            assertEquals(1, violations.size());

            assertEquals(
                    "Catch copy can't be blank",
                    violations.iterator().next().getMessage());
        }

        @Test
        public void conceptが空の場合バリデーションエラーが発生する() {

            prototypeForm.setConcept("");

            Set<ConstraintViolation<PrototypeForm>> violations =
                    validator.validate(prototypeForm);

            assertEquals(1, violations.size());

            assertEquals(
                    "Concept can't be blank",
                    violations.iterator().next().getMessage());
        }

        @Test
        public void imageが空の場合バリデーションエラーが発生する() {

            prototypeForm.setImage(null);

            Set<ConstraintViolation<PrototypeForm>> violations =
                    validator.validate(prototypeForm);

            assertEquals(1, violations.size());

            assertEquals(
                    "Image can't be blank",
                    violations.iterator().next().getMessage());
        }
    }
}