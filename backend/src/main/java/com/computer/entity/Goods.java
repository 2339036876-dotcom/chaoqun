package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Timestamp;

@Data
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Integer goodsId;
    private String title;
    private String img;
    private String description;
    private Double priceAgo;
    private Double price;
    private Integer sales;
    private Integer inventory;
    @TableField("`type`")
    private Integer type;
    private Integer hits;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
    private String customizeField;
    private Integer userId;
}
