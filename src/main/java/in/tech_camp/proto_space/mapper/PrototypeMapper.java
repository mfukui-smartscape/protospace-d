package in.tech_camp.proto_space.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.Prototype;

// PrototypeMapper（共有する形）
@Mapper
public interface PrototypeMapper {

    @Insert("INSERT INTO prototypes (name, catch_copy, concept, image_name, user_id) " +
            "VALUES (#{name}, #{catchCopy}, #{concept}, #{imageName}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Prototype prototype);

    @Select("SELECT * FROM prototypes WHERE id = #{id}")
    Prototype findById(Long id);

    @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
    List<Prototype> findByUserId(Long userId);

    @Select("SELECT * FROM prototypes")
    List<Prototype> findAll();
}
