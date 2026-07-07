package com.example.media.entity;

import lombok.Data;
import java.util.Date;

/**
 * 影视记录实体类
 */
@Data
public class Movie {

    private Integer id;          // 主键ID

    private Integer userId;      // 用户ID

    private String name;         // 名称

    private Integer score;       // 评分

    private String tags;         // 标签（空格分隔）

    private String status;       // 状态（想看/在看/已看完）

    private Date finishTime;     // 完成时间

    private String comment;      // 简评

    private String cover;        // 封面

    private Date createTime;     // 创建时间

    private Date updateTime;     // 更新时间
}