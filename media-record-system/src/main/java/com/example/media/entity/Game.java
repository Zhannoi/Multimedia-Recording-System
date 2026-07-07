package com.example.media.entity;

import lombok.Data;
import java.util.Date;

/**
 * 游戏记录实体类
 */
@Data
public class Game {

    private Integer id;
    private Integer userId;
    private String name;
    private Integer score;
    private String tags;
    private String status;       // 想玩/在玩/已通关
    private Date finishTime;
    private String comment;
    private String cover;
    private Date createTime;
    private Date updateTime;
}