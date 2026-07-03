package in.tech_camp.proto_space.repository;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.proto_space.entity.Prototype;

@Mapper
public interface PrototypeMapper {
    void insert(Prototype prototype);
    Prototype findById(Long id);
    List<Prototype> findByUserId(Long userId);
    List<Prototype> findAll();

    @Update("""
        UPDATE prototypes
        SET name = #{name},
            catch_copy = #{catchCopy},
            concept = #{concept}
        WHERE id = #{id}
        """)
    void update(Prototype prototype);
}