package com.example.profilessystem.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ProgressMapper {

    @Select("SELECT data FROM user_progress WHERE username=#{username}")
    String findDataByUsername(String username);

    @Insert("INSERT INTO user_progress(username, data) VALUES(#{username}, #{data}) "
            + "ON DUPLICATE KEY UPDATE data = #{data}")
    int save(@Param("username") String username, @Param("data") String data);
}
