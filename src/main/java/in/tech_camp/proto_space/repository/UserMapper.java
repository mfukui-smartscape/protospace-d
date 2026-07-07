package in.tech_camp.proto_space.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.UserEntity;

@Mapper
public interface UserMapper {

    @Insert("""
        INSERT INTO users (email, password, name, profile, affiliation, position)
        VALUES (#{email}, #{password}, #{name}, #{profile}, #{affiliation}, #{position})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity user);

    @Select("""
        SELECT * FROM users WHERE id = #{id}
        """)
    UserEntity  findById(Long id);

    @Select("""
        SELECT * FROM users WHERE email = #{email}
        """)
    UserEntity findByEmail(String email);

    @Select("""
        SELECT COUNT(*) FROM users WHERE email = #{email}
        """)
    int countByEmail(String email);
}