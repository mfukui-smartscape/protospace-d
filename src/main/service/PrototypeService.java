
package in.tech_camp.proto_space.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.form.PrototypeForm;
import in.tech_camp.proto_space.repository.PrototypeRepository;

@Service
public class PrototypeService {

    @Autowired
    private PrototypeRepository prototypeRepository;

    public Prototype findById(Long id) {

        return prototypeRepository.findById(id);
    }

    public void update(
            Long id,
            PrototypeForm form,
            String imageName) {

        Prototype prototype =
                prototypeRepository.findById(id);

        prototype.setName(form.getName());
        prototype.setCatchCopy(form.getCatchCopy());
        prototype.setConcept(form.getConcept());
        prototype.setImageName(imageName);

        prototypeRepository.update(prototype);
    }
}
