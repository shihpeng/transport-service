package com.jkopay.industry.transport.repository;

import com.jkopay.industry.transport.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(gmt_created, name) VALUES (CURRENT_TIMESTAMP(), #{name})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insert(User user);

    @Select("SELECT id, gmt_created, name FROM user WHERE id = #{id}")
    User findById(long id);

    @Select("SELECT id, gmt_created, name FROM user WHERE name = #{name}")
    User findByName(String name);
}
