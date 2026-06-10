package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Timestamp;

@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private String orderNumber;
    private Integer goodsId;
    private Double priceAgo;
    private Integer num;
    private Double priceCount;
    private String contactName;
    private String contactPhone;
    private String contactAddress;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
    private String description;
    private String state;
    private String remark;
    private String deliveryState;
    private Double vipDiscount;
}
