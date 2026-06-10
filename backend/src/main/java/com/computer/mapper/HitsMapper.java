package com.computer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.computer.entity.Hits;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HitsMapper extends BaseMapper<Hits> {
}
