package in.tech_camp.proto_space.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.entity.TagEntity;
import in.tech_camp.proto_space.form.PrototypeSearchForm;
import in.tech_camp.proto_space.repository.PrototypeMapper;
import in.tech_camp.proto_space.repository.TagMapper;

@Service
public class PrototypeService {

    private final PrototypeMapper prototypeMapper;
    private final TagMapper tagMapper;

    public PrototypeService(PrototypeMapper prototypeMapper, TagMapper tagMapper) {
        this.prototypeMapper = prototypeMapper;
        this.tagMapper = tagMapper;
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

    // ===== タグ・検索用に追加 =====

    public List<PrototypeEntity> search(PrototypeSearchForm form) {
        return prototypeMapper.search(form);
    }

    public List<TagEntity> findAllTags() {
        return tagMapper.findAll();
    }

    @Transactional
    public void saveWithTags(PrototypeEntity prototype, List<Long> tagIds) {
        if (prototype == null) {
            throw new IllegalArgumentException("prototype must not be null");
        }
        prototypeMapper.insert(prototype);   // useGeneratedKeys で id が入る
        if (tagIds != null && !tagIds.isEmpty()) {
            tagMapper.addPrototypeTags(prototype.getId(), tagIds);
        }
    }

    @Transactional
    public void updateWithTags(PrototypeEntity prototype, List<Long> tagIds) {
        if (prototype == null) {
            throw new IllegalArgumentException("prototype must not be null");
        }
        prototypeMapper.update(prototype);
        tagMapper.deletePrototypeTags(prototype.getId());
        if (tagIds != null && !tagIds.isEmpty()) {
            tagMapper.addPrototypeTags(prototype.getId(), tagIds);
        }
    }
}