package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("collect")
public class Collect {

    @TableId(type = IdType.AUTO)
    private Integer collectId;
    private Integer userId;
    private Integer sourceId;
    @TableField("source_table")
    private String sourceTable;
    private String title;
    private LocalDateTime createTime;
}
