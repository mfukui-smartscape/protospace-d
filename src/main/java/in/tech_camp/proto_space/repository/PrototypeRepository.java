package in.tech_camp.proto_space.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.proto_space.entity.Prototype;

@Mapper
public interface PrototypeRepository {
  @Insert("INSERT INTO prototypes (name, catch_copy, concept, image_name) VALUES (#{name}, #{catchCopy}, #{concept}, #{imageName})")
  @Options(useGeneratedKeys = true,keyProperty = "id")
  void insert(Prototype prototype);
  
  @Select("SELECT * FROM prototypes")
  List<Prototype> findAll();

}
