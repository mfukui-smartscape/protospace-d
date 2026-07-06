
package in.tech_camp.proto_space.factory;

import com.github.javafaker.Faker;

import in.tech_camp.proto_space.form.UserForm;

public class UserFormFactory {

    private static final Faker faker = new Faker();

    public static UserForm createUser() {

        UserForm form = new UserForm();

        form.setEmail(faker.internet().emailAddress());
        form.setPassword("password");
        form.setName(faker.name().username());
        form.setProfile("profile");
        form.setAffiliation("affiliation");
        form.setPosition("position");

        return form;
    }
}
