package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("score")
public class Score {
    @TableId(type = IdType.AUTO)
    private Integer scoreId;
    private Integer userId;
    private String nickname;
    private Double scoreNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @TableField("source_table")
    private String sourceTable;
    private Integer sourceId;
}
