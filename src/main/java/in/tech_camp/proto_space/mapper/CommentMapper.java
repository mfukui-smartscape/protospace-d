package in.tech_camp.proto_space.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import in.tech_camp.proto_space.entity.Comment;

// CommentMapper（共有する形）
@Mapper
public interface CommentMapper {
    void insert(Comment comment);
    List<Comment> findByPrototypeId(Long prototypeId);
}
