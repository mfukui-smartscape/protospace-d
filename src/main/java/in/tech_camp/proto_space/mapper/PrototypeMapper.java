package in.tech_camp.proto_space.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import in.tech_camp.proto_space.entity.Prototype;
// PrototypeMapper（共有する形）
@Mapper
public interface PrototypeMapper {
    void insert(Prototype prototype);
    Prototype findById(Long id);
    List<Prototype> findByUserId(Long userId);
    List<Prototype> findAll();
}