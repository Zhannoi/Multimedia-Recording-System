package com.example.media.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
public class User {

    private Integer id;          // 用户ID

    private String username;     // 用户名

    private String password;     // 密码

    private Date createTime;     // 创建时间
}