package com.neu.easypay.mapper;

import com.neu.easypay.model.UserAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserAccountMapper {

    @Insert("INSERT INTO users (uuid, username, password, email, name) VALUES (#{uuid}, #{username}, #{password}, #{email}, #{name})")
    void create(UserAccount userAccount);

    @Update("UPDATE users SET balance = #{balance} WHERE uuid = #{userUUID}")
    boolean update(String userUUID, Double balance);

    @Select("SELECT * FROM users WHERE uuid = #{userUUID}")
    UserAccount getByUserUUID(String userUUID);

    @Select("SELECT * FROM users WHERE username = #{username} LIMIT 1")
    UserAccount queryByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email} LIMIT 1")
    UserAccount queryByEmail(String email);

    @Select("SELECT * FROM users WHERE username = #{username} OR email = #{email} LIMIT 1")
    UserAccount queryByUsernameOrEmail(String username, String email);

    @Select("SELECT * FROM users WHERE uuid = #{uuid}")
    UserAccount queryByUuid(String uuid);

    @Select("SELECT balance FROM users WHERE uuid = #{uuid}")
    Double queryBalance(String uuid);
}
