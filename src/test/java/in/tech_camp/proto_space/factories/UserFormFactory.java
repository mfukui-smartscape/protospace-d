package in.tech_camp.proto_space.factories;

import com.github.javafaker.Faker;
import in.tech_camp.proto_space.form.UserForm;

public class UserFormFactory {

  private static final Faker faker = new Faker();

  public static UserForm createUser() {
    UserForm userForm = new UserForm();

    userForm.setName(faker.name().username());

    userForm.setEmail(faker.internet().emailAddress());

    String password = faker.internet().password(6, 12);
    userForm.setPassword(password);
    userForm.setPasswordConfirmation(password);

    userForm.setProfile(faker.lorem().sentence());

    userForm.setRole(faker.company().profession());

    userForm.setPosition(faker.job().position());

    return userForm;
  }
}

