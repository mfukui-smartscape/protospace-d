package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.entity.TagEntity;
import in.tech_camp.proto_space.form.PrototypeSearchForm;

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
    @Results(id = "prototypeResult", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "tags", column = "id",
                many = @Many(select = "findTagsByPrototypeId"))
    })
    PrototypeEntity findById(Long id);

    @Select("""
        SELECT p.*, u.name AS user_name
        FROM prototypes p
        JOIN users u ON u.id = p.user_id
        WHERE p.user_id = #{userId}
        ORDER BY p.created_at DESC
        """)
    @ResultMap("prototypeResult")
    List<PrototypeEntity> findByUserId(Long userId);

    @Select("""
        SELECT p.*, u.name AS user_name
        FROM prototypes p
        JOIN users u ON u.id = p.user_id
        ORDER BY p.created_at DESC
        """)
    @ResultMap("prototypeResult")
    List<PrototypeEntity> findAll();

    @SelectProvider(type = PrototypeSqlProvider.class, method = "search")
    @ResultMap("prototypeResult")
    List<PrototypeEntity> search(PrototypeSearchForm form);

    @Select("""
        SELECT t.id, t.name
        FROM tags t
        JOIN prototypes_tags pt ON pt.tag_id = t.id
        WHERE pt.prototype_id = #{prototypeId}
        ORDER BY t.id
        """)
    List<TagEntity> findTagsByPrototypeId(Long prototypeId);

    @Update("UPDATE prototypes "
          + "SET name = #{name}, catch_copy = #{catchCopy}, "
          + "concept = #{concept}, image_name = #{imageName}, "
          + "updated_at = CURRENT_TIMESTAMP "
          + "WHERE id = #{id}")
    void update(PrototypeEntity prototype);

    @Delete("DELETE FROM prototypes WHERE id = #{id}")
    void delete(Long id);
}