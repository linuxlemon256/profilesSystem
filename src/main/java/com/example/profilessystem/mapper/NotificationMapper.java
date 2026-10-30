package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.Notification;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("INSERT INTO user_notification(username,sender_name,content) VALUES(#{username},#{senderName},#{content})")
    int insert(Notification notification);
    @Select("SELECT id,username,sender_name,content,read_status,created_at FROM user_notification WHERE username=#{username} ORDER BY created_at DESC LIMIT 100")
    List<Notification> findByUser(String username);
    @Update("UPDATE user_notification SET read_status=1 WHERE id=#{id} AND username=#{username}")
    int markRead(@Param("id") long id,@Param("username") String username);
}
