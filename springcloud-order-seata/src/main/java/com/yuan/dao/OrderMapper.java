package com.yuan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuan.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
