package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.Prototype;

@Mapper
public interface PrototypeRepository {
  @Insert("INSERT INTO prototypes (name, catchCopy, concept, imageName) VALUES (#{name}, #{catch_copy}, #{concept}, #{image_name})")
  @Options(useGeneratedKeys = true,keyProperty = "id")
  void insert(Prototype prototype);
  
  @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
  List<Prototype> findAll();

}
