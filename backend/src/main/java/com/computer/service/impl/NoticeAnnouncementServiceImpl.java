package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.NoticeAnnouncement;
import com.computer.mapper.NoticeAnnouncementMapper;
import com.computer.service.NoticeAnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class NoticeAnnouncementServiceImpl extends ServiceImpl<NoticeAnnouncementMapper, NoticeAnnouncement> implements NoticeAnnouncementService {
}
