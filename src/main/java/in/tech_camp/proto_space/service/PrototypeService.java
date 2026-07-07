package in.tech_camp.proto_space.service;

import java.util.List;

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

    public List<Prototype> findAll() {
        return prototypeMapper.findAll();
    }

    public List<Prototype> findByUserId(Long userId) {
        return prototypeMapper.findByUserId(userId);
    }

    public Prototype findById(Long id) {
        return prototypeMapper.findById(id);
    }
}