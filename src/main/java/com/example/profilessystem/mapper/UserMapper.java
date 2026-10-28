package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UserMapper {
    @Select("select username, password, storage, code, isin, email, reason, created_at from user where username=#{name}")
    User findByName(String name);
    @Select("select username, password, storage, code, isin, email, reason, created_at from user where email=#{email}")
    User findByEmail(String email);
    @Select("select username, password, storage, code, isin, email, reason, created_at from user where code=#{code}")
    User findByCode(String code);
    @Select("select username, password, storage, code, isin, email, reason, created_at from user where isin=#{isin}")
    ArrayList<User> findByIsIn(int isin);
    @Select("select username, password, storage, code, isin, email, reason, created_at from user")
    ArrayList<User> findAll();
    @Insert("INSERT INTO user(username, password, storage, code, isin, email, reason) VALUES(#{username},#{password},#{storage},#{code},#{isin},#{email},#{reason})")
    int insert(User user);
    @Delete("DELETE FROM user WHERE username=#{name};")
    int deleteByName(String name);
    @Delete("DELETE FROM user WHERE code=#{code};")
    int deleteByCode(String code);
    @Update("UPDATE user SET isin=#{isin} WHERE username=#{username}")
    int updateIsin(@Param("username") String username, @Param("isin") int isin);
    @Update("UPDATE user SET code=#{code} WHERE username=#{username}")
    int updateCode(@Param("username") String username, @Param("code") String code);
}
