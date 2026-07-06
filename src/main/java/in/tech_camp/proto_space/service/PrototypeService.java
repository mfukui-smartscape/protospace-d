package in.tech_camp.proto_space.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.repository.PrototypeRepository;

@Service
public class PrototypeService {

    private final PrototypeRepository prototypeRepository;

    public PrototypeService(PrototypeRepository prototypeRepository) {
        this.prototypeRepository = prototypeRepository;
    }

    public List<Prototype> findAll() {
        return prototypeRepository.findAll();
    }

    public List<Prototype> findByUserId(Long userId) {
        return prototypeRepository.findByUserId(userId);
    }

    public Prototype findById(Long id) {
        return prototypeRepository.findById(id);
    }
}