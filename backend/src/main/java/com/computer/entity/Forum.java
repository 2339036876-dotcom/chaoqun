package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("forum")
public class Forum {
    @TableId(type = IdType.AUTO)
    private Integer forumId;
    private Integer display;
    private Integer userId;
    private String nickname;
    private Integer praiseLen;
    private Integer hits;
    private String title;
    private String tag;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String avatar;
    @TableField("`type`")
    private String type;
    private Integer istop;
}
