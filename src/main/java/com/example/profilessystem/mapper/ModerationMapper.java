package com.example.profilessystem.mapper;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;
@Mapper
public interface ModerationMapper {
 @Select("SELECT id, word FROM sensitive_word ORDER BY id DESC LIMIT 20001")
 List<Map<String,Object>> all();
 @Insert("INSERT IGNORE INTO sensitive_word(word, normalized) VALUES(#{word},#{normalized})")
 int add(@Param("word") String word,@Param("normalized") String normalized);
 @Delete("DELETE FROM sensitive_word WHERE id=#{id}") int delete(long id);
}
