package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.GoodsType;
import com.computer.mapper.GoodsTypeMapper;
import com.computer.service.GoodsTypeService;
import org.springframework.stereotype.Service;

@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {
}
