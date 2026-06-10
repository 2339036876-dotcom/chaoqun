package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("merchant_customer_service")
public class MerchantCustomerService {
    @TableId(type = IdType.AUTO)
    private Integer merchantCustomerServiceId;
    private Integer userId;
    private Integer goodsId;
    private String goodsTitle;
    private String sessionId;
    private Integer merchantUsers;
    private String senderType;
    private String messageContent;
    private String readState;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
