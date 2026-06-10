package com.computer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("notice_announcement")
public class NoticeAnnouncement {
    @TableId(type = IdType.AUTO)
    private Integer noticeAnnouncementId;
    private Integer merchantUsers;
    private String notificationTitle;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date notificationTime;
    private String notificationContent;
    private Integer hits;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
