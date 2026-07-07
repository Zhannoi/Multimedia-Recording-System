package com.example.media.entity;

import lombok.Data;
import java.util.Date;

/**
 * 书籍记录实体类
 */
@Data
public class Book {

    private Integer id;
    private Integer userId;
    private String name;
    private Integer score;
    private String tags;
    private String status;       // 想看/在看/已看完
    private Date finishTime;
    private String comment;
    private String cover;
    private Date createTime;
    private Date updateTime;
}