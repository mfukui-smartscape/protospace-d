package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.proto_space.entity.Prototype;

@Mapper
public interface PrototypeMapper {

    @Insert("INSERT INTO prototypes (name, catch_copy, concept, image_name, user_id) "
          + "VALUES (#{name}, #{catchCopy}, #{concept}, #{imageName}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Prototype prototype);

    @Select("SELECT * FROM prototypes WHERE id = #{id}")
    Prototype findById(Long id);

    @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
    List<Prototype> findByUserId(Long userId);

    @Select("SELECT * FROM prototypes ORDER BY created_at DESC")
    List<Prototype> findAll();

    @Update("UPDATE prototypes "
          + "SET name = #{name}, catch_copy = #{catchCopy}, "
          + "concept = #{concept}, image_name = #{imageName}, "
          + "updated_at = CURRENT_TIMESTAMP "
          + "WHERE id = #{id}")
    void update(Prototype prototype);

    @Delete("DELETE FROM prototypes WHERE id = #{id}")
    void delete(Long id);
}