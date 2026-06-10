package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.Forum;
import com.computer.mapper.ForumMapper;
import com.computer.service.ForumService;
import org.springframework.stereotype.Service;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {
}
