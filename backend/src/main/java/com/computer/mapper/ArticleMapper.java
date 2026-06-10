package com.computer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.computer.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
