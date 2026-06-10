package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.Slides;
import com.computer.mapper.SlidesMapper;
import com.computer.service.SlidesService;
import org.springframework.stereotype.Service;

@Service
public class SlidesServiceImpl extends ServiceImpl<SlidesMapper, Slides> implements SlidesService {
}
