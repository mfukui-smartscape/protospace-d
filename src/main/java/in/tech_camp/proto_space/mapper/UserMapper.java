package in.tech_camp.proto_space.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.User;

// UserMapper（共有する形）
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (email, password, name, profile, affiliation, position) " +
            "VALUES (#{email}, #{password}, #{name}, #{profile}, #{affiliation}, #{position})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
}
