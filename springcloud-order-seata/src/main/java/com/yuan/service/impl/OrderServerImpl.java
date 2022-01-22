package com.yuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.OrderMapper;
import com.yuan.pojo.Order;
import com.yuan.pojo.Stock;
import com.yuan.service.OrderService;
import com.yuan.api.StockFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServerImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    StockFeignClient stockFeignClient;
    @Autowired
    ObjectMapper objectMapper;
    @SneakyThrows
    @Override
    @GlobalTransactional
    public boolean save(Order order) {
        //远程调用 查看库存
        String s = stockFeignClient.selectStockByName(order.getGoodName());
        Stock stock = objectMapper.readValue(s, new TypeReference<Stock>() {
        });
        if(stock.getTotal()>0){
            //下单
            orderMapper.insert(order);
            //减库存
            int i = stock.getTotal().intValue() - 1;
            stock.setTotal(i);
            stockFeignClient.updateStockByName(stock);
            return true;
        }
        return false;
    }
}
