package in.tech_camp.proto_space.mapper;

import org.apache.ibatis.annotations.Mapper;

import in.tech_camp.proto_space.entity.User;

// UserMapper（共有する形）
@Mapper
public interface UserMapper {
    void insert(User user);
    User findById(Long id);
    User findByEmail(String email);
}