package com.example.media.mapper;

import com.example.media.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口（操作数据库）
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户（用于登录）
     */
    User findByUsername(@Param("username") String username);

    /**
     * 新增用户（注册）
     */
    int insert(User user);

    List<User> findAll();

    int delete(Integer id);

}