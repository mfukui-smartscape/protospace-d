package in.tech_camp.proto_space.repository;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import in.tech_camp.proto_space.entity.Prototype;
// PrototypeMapper（共有する形）
@Mapper
public interface PrototypeMapper {
    @Insert("INSERT INTO prototypes (name, catch_copy, concept, image_name) VALUES (#{name}, #{catchCopy}, #{concept}, #{imageName})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Prototype prototype);
    
    Prototype findById(Long id);
    List<Prototype> findByUserId(Long userId);
    List<Prototype> findAll();
}