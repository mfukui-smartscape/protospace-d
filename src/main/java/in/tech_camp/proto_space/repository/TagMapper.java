package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.TagEntity;

@Mapper
public interface TagMapper {

    @Select("SELECT id, name FROM tags ORDER BY id")
    List<TagEntity> findAll();

    @Insert({
        "<script>",
        "INSERT INTO prototypes_tags (prototype_id, tag_id) VALUES",
        "<foreach item='tid' collection='tagIds' separator=','>",
        "(#{prototypeId}, #{tid})",
        "</foreach>",
        "</script>"
    })
    void addPrototypeTags(@Param("prototypeId") Long prototypeId,
                          @Param("tagIds") List<Long> tagIds);

    // 編集時のタグ貼り替え用（削除→再挿入）
    @org.apache.ibatis.annotations.Delete(
        "DELETE FROM prototypes_tags WHERE prototype_id = #{prototypeId}")
    void deletePrototypeTags(@Param("prototypeId") Long prototypeId);
}