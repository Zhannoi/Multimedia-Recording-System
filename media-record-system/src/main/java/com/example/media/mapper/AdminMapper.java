package com.example.media.mapper;

import com.example.media.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员Mapper
 */
@Mapper
public interface AdminMapper {

    /**
     * 根据用户名查询管理员（登录）
     */
    Admin findByUsername(@Param("username") String username);
}