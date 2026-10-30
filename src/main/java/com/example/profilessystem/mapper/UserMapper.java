package com.example.profilessystem.mapper;

import com.example.profilessystem.entity.User;
import com.example.profilessystem.entity.UserAdminUpdateBody;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UserMapper {
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user where username=#{name}")
    User findByName(String name);
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user where email=#{email}")
    User findByEmail(String email);
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user where code=#{code}")
    User findByCode(String code);
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user where isin=#{isin}")
    ArrayList<User> findByIsIn(int isin);
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user")
    ArrayList<User> findAll();
    @Insert("INSERT INTO user(username, password, storage, code, isin, level, email, reason) VALUES(#{username},#{password},#{storage},#{code},#{isin},#{level},#{email},#{reason})")
    int insert(User user);
    @Delete("DELETE FROM user WHERE username=#{name};")
    int deleteByName(String name);
    @Delete("DELETE FROM user WHERE code=#{code};")
    int deleteByCode(String code);
    @Update("UPDATE user SET isin=#{isin}, level=CASE WHEN #{isin}=1 AND level=0 THEN 1 ELSE level END WHERE username=#{username}")
    int updateIsin(@Param("username") String username, @Param("isin") int isin);
    @Update("UPDATE user SET code=#{code} WHERE username=#{username}")
    int updateCode(@Param("username") String username, @Param("code") String code);
    @Update("UPDATE user SET password=#{password} WHERE username=#{username}")
    int updatePassword(@Param("username") String username, @Param("password") String password);
    @Update("UPDATE user SET recovery_type=#{type}, recovery_reason=#{reason}, recovery_status=1, recovery_created_at=CURRENT_TIMESTAMP WHERE username=#{username}")
    int createRecoveryRequest(@Param("username") String username, @Param("type") String type, @Param("reason") String reason);
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user where recovery_status=1 order by recovery_created_at asc")
    java.util.List<User> findPendingRecovery();
    @Select("select username, password, storage, code, isin, level, email, reason, created_at, recovery_type, recovery_reason, recovery_status, recovery_created_at from user where recovery_type is not null order by recovery_created_at desc")
    java.util.List<User> findAllRecovery();
    @Update("UPDATE user SET recovery_status=#{status} WHERE username=#{username} AND recovery_status=1")
    int updateRecoveryStatus(@Param("username") String username, @Param("status") int status);
    @Update({"<script>", "UPDATE user", "<set>",
            "<if test='update.email != null'>email=#{update.email},</if>",
            "<if test='update.reason != null'>reason=#{update.reason},</if>",
            "<if test='update.level != null'>level=#{update.level},</if>",
            "<if test='update.code != null'>code=#{update.code},</if>",
            "<if test='update.password != null'>password=#{update.password},</if>",
            "</set>", "WHERE username=#{username}", "</script>"})
    int updateByAdmin(@Param("username") String username, @Param("update") UserAdminUpdateBody update);
}
