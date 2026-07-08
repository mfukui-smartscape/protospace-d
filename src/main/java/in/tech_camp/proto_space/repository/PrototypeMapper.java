package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.proto_space.entity.PrototypeEntity;

@Mapper
public interface PrototypeMapper {

    @Insert("INSERT INTO prototypes (name, catch_copy, concept, image_name, user_id) "
          + "VALUES (#{name}, #{catchCopy}, #{concept}, #{imageName}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PrototypeEntity prototype);

    @Select("""
        SELECT p.*, u.name AS user_name
        FROM prototypes p
        JOIN users u ON u.id = p.user_id
        WHERE p.id = #{id}
        """)
    PrototypeEntity findById(Long id);

    @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
    List<PrototypeEntity> findByUserId(Long userId);

    @Select("""
        SELECT p.*, u.name AS user_name
        FROM prototypes p
        JOIN users u ON u.id = p.user_id
        ORDER BY p.created_at DESC
        """)
    List<PrototypeEntity> findAll();

    @Update("UPDATE prototypes "
          + "SET name = #{name}, catch_copy = #{catchCopy}, "
          + "concept = #{concept}, image_name = #{imageName}, "
          + "updated_at = CURRENT_TIMESTAMP "
          + "WHERE id = #{id}")
    void update(PrototypeEntity prototype);

    @Delete("DELETE FROM prototypes WHERE id = #{id}")
    void delete(Long id);
}