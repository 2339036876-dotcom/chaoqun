package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.ArticleType;
import com.computer.mapper.ArticleTypeMapper;
import com.computer.service.ArticleTypeService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements ArticleTypeService {
}
