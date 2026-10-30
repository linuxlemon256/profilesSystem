package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.Group;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.ArrayList;

@Mapper
public interface GroupMapper {
    @Select("SELECT g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status, COUNT(m.username) AS member_count "
            + "FROM study_group g LEFT JOIN study_group_member m ON m.group_id=g.id "
            + "WHERE g.approval_status=1 GROUP BY g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status ORDER BY g.created_at DESC")
    List<Group> findAll();

    @Select("SELECT g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status, COUNT(m2.username) AS member_count FROM study_group g JOIN study_group_member mine ON mine.group_id=g.id AND mine.username=#{username} LEFT JOIN study_group_member m2 ON m2.group_id=g.id WHERE g.approval_status=1 GROUP BY g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status ORDER BY g.created_at DESC")
    List<Group> findMine(String username);

    @Select("SELECT g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status, COUNT(m.username) AS member_count "
            + "FROM study_group g LEFT JOIN study_group_member m ON m.group_id=g.id "
            + "WHERE g.id=#{id} GROUP BY g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status")
    Group findById(long id);

    @Insert("INSERT INTO study_group(name, description, owner_username, approval_status) VALUES(#{name},#{description},#{ownerUsername},0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Group group);

    @Insert("INSERT INTO study_group_member(group_id, username, role) VALUES(#{groupId},#{username},#{role})")
    int addMember(@Param("groupId") long groupId, @Param("username") String username, @Param("role") String role);

    @Delete("DELETE FROM study_group_member WHERE group_id=#{groupId} AND username=#{username}")
    int removeMember(@Param("groupId") long groupId, @Param("username") String username);

    @Select("SELECT g.id, g.name, g.description, g.owner_username, g.created_at, g.approval_status FROM study_group g WHERE g.approval_status=0 ORDER BY g.created_at ASC")
    List<Group> findPending();

    @Update("UPDATE study_group SET approval_status=#{status} WHERE id=#{id}")
    int updateApproval(@Param("id") long id, @Param("status") int status);

    @Select("SELECT COUNT(*) FROM study_group_member WHERE group_id=#{groupId} AND username=#{username}")
    int isMember(@Param("groupId") long groupId, @Param("username") String username);

    @Select("SELECT username FROM study_group_member WHERE group_id=#{groupId} ORDER BY joined_at ASC")
    ArrayList<String> findMemberNames(long groupId);
}
