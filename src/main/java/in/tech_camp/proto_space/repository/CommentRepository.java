package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.Comment;

@Mapper
public interface CommentRepository {

    @Insert("""
        INSERT INTO comments (content, user_id, prototype_id)
        VALUES (#{content}, #{userId}, #{prototypeId})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Comment comment);

    @Select("""
        SELECT c.*, u.name AS user_name
        FROM comments c
        JOIN users u ON u.id = c.user_id
        WHERE c.prototype_id = #{prototypeId}
        ORDER BY c.created_at ASC
        """)
    List<Comment> findByPrototypeId(Long prototypeId);
}
