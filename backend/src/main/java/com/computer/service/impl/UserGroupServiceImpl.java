package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.UserGroup;
import com.computer.mapper.UserGroupMapper;
import com.computer.service.UserGroupService;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {
}
