package in.tech_camp.proto_space.service;

import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.repository.PrototypeRepository;

@Service
public class PrototypeService {

    private final PrototypeRepository prototypeMapper;

    public PrototypeService(PrototypeRepository prototypeMapper) {
        this.prototypeMapper = prototypeMapper;
    }

    public void save(Prototype prototype) {
        if (prototype == null) {
            throw new IllegalArgumentException("prototype must not be null");
        }
        prototypeMapper.insert(prototype);
    }
}