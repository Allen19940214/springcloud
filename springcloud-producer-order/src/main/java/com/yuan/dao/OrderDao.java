package com.yuan.dao;

import com.yuan.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface OrderDao {
    List<Order> findAll();
    Order findById(@Param("orderId")String id);
    int addOrder(Order order);
    int updateById(Order order);
    int deleteById(@Param("orderId")String id);

    //冗余表语句
    int addOrderToBackup(Order order);
    int updateByIdBackup(Order order);
    List<Order> selectByCondition(Map map);
}
