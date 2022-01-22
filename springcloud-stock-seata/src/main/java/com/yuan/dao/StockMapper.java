package com.yuan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuan.pojo.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}
