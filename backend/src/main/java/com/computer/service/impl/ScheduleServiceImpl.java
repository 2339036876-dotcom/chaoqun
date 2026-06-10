package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.Schedule;
import com.computer.mapper.ScheduleMapper;
import com.computer.service.ScheduleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;
}
