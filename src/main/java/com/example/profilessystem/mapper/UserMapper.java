package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select username, password, storage, code from user where username=#{name}")
    Manager findByName(String name);
    @Insert("INSERT INTO user(username, password, storage, code) VALUES(#{username},#{password},#{storage},#{code})")
    int insert(User user);
    @Delete("DELETE FROM user WHERE username=#{name};")
    int deleteByName(String name);
    @Delete("DELETE FROM user WHERE code=#{code};")
    int deleteByCode(String code);

}
