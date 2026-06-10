package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("forum_type")
public class ForumType {

    @TableId(type = IdType.AUTO)
    private Integer typeId;
    private String name;
    private String description;
    private LocalDateTime createTime;
}
