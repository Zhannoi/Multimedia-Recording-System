package com.example.media.entity;

import lombok.Data;

/**
 * 管理员实体类
 */
@Data
public class Admin {

    private Integer id;          // 管理员ID

    private String username;     // 账号

    private String password;     // 密码
}