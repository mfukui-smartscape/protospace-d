package in.tech_camp.proto_space.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.repository.PrototypeMapper;

@Service
public class PrototypeService {

    private final PrototypeMapper prototypeMapper;

    public PrototypeService(PrototypeMapper prototypeMapper) {
        this.prototypeMapper = prototypeMapper;
    }

    public void save(PrototypeEntity prototype) {
        if (prototype == null) {
            throw new IllegalArgumentException("prototype must not be null");
        }
        prototypeMapper.insert(prototype);
    }

    public List<PrototypeEntity> findAll() {
        return prototypeMapper.findAll();
    }

    public List<PrototypeEntity> findByUserId(Long userId) {
        return prototypeMapper.findByUserId(userId);
    }

    public PrototypeEntity findById(Long id) {
        return prototypeMapper.findById(id);
    }

    public void update(PrototypeEntity prototype) {
        if (prototype == null) {
            throw new IllegalArgumentException("prototype must not be null");
        }
        prototypeMapper.update(prototype);
    }

    public void delete(Long id) {
        prototypeMapper.delete(id);
    }
}