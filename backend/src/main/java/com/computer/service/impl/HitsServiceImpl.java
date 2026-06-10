package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.Hits;
import com.computer.mapper.HitsMapper;
import com.computer.service.HitsService;
import org.springframework.stereotype.Service;

@Service
public class HitsServiceImpl extends ServiceImpl<HitsMapper, Hits> implements HitsService {
}
