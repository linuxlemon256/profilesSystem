package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.Manager;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ManagerMapper {
    @Select("select count(*) from manager")
    int count();
    @Select("select managername, password, level from manager where managername=#{name}")
    Manager findByName(String name);
    @Select("SELECT managername, password, level FROM manager WHERE id=#{id}")
    Manager findById(int id);
    @Insert("INSERT INTO manager(managername, password, level) VALUES(#{managername},#{password},#{level})")
    int insert(Manager manager);
    @Delete("DELETE FROM manager WHERE managername=#{name};")
    int deleteByName(String name);
    @Update("UPDATE manager SET managername=#{newManagerName} WHERE managername=#{oldManagerName}")
    int updateNameByName(String oldManagerName, String newManagerName);
    @Update("UPDATE manager SET managername=#{newManagerName} WHERE id=#{id}")
    int updateNameById(int id, String newManagerName);
}
