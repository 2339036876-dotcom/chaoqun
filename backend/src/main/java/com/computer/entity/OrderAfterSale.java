package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Timestamp;

@Data
@TableName("order_after_sale")
public class OrderAfterSale {
    @TableId(type = IdType.AUTO)
    private Integer orderAfterSaleId;
    private Integer orderId;
    private String afterState;
    private String afterStateReply;
    @com.baomidou.mybatisplus.annotation.TableField("`type`")
    private String type;
    private String contentDesc;
    private String imgs;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}
