package in.tech_camp.proto_space.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.proto_space.entity.User;

@Mapper
public interface UserRepository {

    @Insert("""
        INSERT INTO users (email, password, name, profile, affiliation, position)
        VALUES (#{email}, #{password}, #{name}, #{profile}, #{affiliation}, #{position})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    @Update("""
        UPDATE users
        SET name = #{name}, profile = #{profile}, affiliation = #{affiliation}, position = #{position},
            updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    void update(User user);
}
