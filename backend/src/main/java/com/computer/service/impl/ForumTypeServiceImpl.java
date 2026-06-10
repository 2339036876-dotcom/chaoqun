package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.ForumType;
import com.computer.mapper.ForumTypeMapper;
import com.computer.service.ForumTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ForumTypeServiceImpl extends ServiceImpl<ForumTypeMapper, ForumType> implements ForumTypeService {

    @Resource
    private ForumTypeMapper forumTypeMapper;
}
