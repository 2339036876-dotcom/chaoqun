package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("logistics_delivery")
public class LogisticsDelivery {
    @TableId(type = IdType.AUTO)
    private Integer logisticsDeliveryId;
    private String orderNumber;
    private String productName;
    private String purchaseQuantity;
    private Double totalTransactionAmount;
    private String deliveryStatus;
    private String signingStatus;
    private String contactName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
