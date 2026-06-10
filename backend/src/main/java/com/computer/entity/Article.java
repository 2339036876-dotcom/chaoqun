package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Timestamp;

@Data
@TableName("article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer articleId;
    private String title;
    @TableField("`type`")
    private Integer type;
    private Integer hits;
    private Integer praiseLen;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
    private String source;
    private String url;
    private String content;
    private String img;
    private String description;
}
