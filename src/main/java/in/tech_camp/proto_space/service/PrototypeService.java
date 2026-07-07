package in.tech_camp.proto_space.service;

import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.repository.PrototypeMapper;

@Service
public class PrototypeService {

    private final PrototypeMapper prototypeMapper;

    public PrototypeService(PrototypeMapper prototypeMapper) {
        this.prototypeMapper = prototypeMapper;
    }

    public void save(Prototype prototype) {
        if (prototype == null) {
            throw new IllegalArgumentException("prototype must not be null");
        }
        prototypeMapper.insert(prototype);
    }
}