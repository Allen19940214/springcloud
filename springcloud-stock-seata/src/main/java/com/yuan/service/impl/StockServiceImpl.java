package com.yuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.dao.StockMapper;
import com.yuan.pojo.Stock;
import com.yuan.service.StockService;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

}
