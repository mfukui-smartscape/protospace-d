package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.Prototype;

// PrototypeRepository（共有する形）
@Mapper
public interface PrototypeRepository {

    @Insert("INSERT INTO prototypes (name, catch_copy, concept, image_name, user_id) " +
            "VALUES (#{name}, #{catchCopy}, #{concept}, #{imageName}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Prototype prototype);

    @Select("""
        SELECT
            p.*,
            u.name AS user_name
        FROM prototypes p
        JOIN users u
          ON p.user_id = u.id
        WHERE p.id = #{id}
        """)
    Prototype findById(Long id);

    @Select("""
        SELECT
            p.*,
            u.name AS user_name
        FROM prototypes p
        JOIN users u
          ON p.user_id = u.id
        WHERE p.user_id = #{userId}
        """)
    List<Prototype> findByUserId(Long userId);

    @Select("""
        SELECT
            p.*,
            u.name AS user_name
        FROM prototypes p
        JOIN users u
          ON p.user_id = u.id
        """)
    List<Prototype> findAll();
}