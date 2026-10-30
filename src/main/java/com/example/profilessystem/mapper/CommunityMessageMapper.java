package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.CommunityMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommunityMessageMapper {
    @Insert("INSERT INTO community_message(request_id, sender_type, sender_name, content) "
            + "VALUES(#{requestId}, #{senderType}, #{senderName}, #{content})")
    int insert(CommunityMessage message);

    @Select("SELECT id, request_id, sender_type, sender_name, content, created_at FROM community_message "
            + "WHERE request_id=#{requestId} ORDER BY created_at ASC, id ASC")
    List<CommunityMessage> findByRequestId(long requestId);

    @Select("SELECT m.id, m.request_id, m.sender_type, m.sender_name, m.content, m.created_at "
            + "FROM community_message m JOIN community_request r ON r.id=m.request_id "
            + "WHERE (r.username=#{username} AND r.username IS NOT NULL AND r.username <> '') "
            + "OR (#{email} <> '' AND r.contact=#{email}) ORDER BY m.created_at ASC, m.id ASC")
    List<CommunityMessage> findForUser(@Param("username") String username, @Param("email") String email);
}
