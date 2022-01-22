package com.yuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.pojo.Stock;
import com.yuan.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class TestController {
    @Autowired
    StockService stockService;
    @Autowired
    ObjectMapper objectMapper;
    @RequestMapping("/selectStockByName")
    public String selectStockByName(@RequestParam("goodName") String name) throws JsonProcessingException {

        QueryWrapper<Stock> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("good_name",name);
        Stock one = stockService.getOne(QueryWrapper);
        String s = objectMapper.writeValueAsString(one);
        return s;
    }
    @RequestMapping("/updateStockByName")
    public String updateStockByName(@RequestBody Stock stock) throws JsonProcessingException {
        boolean update = stockService.update(stock, null);
        String s = objectMapper.writeValueAsString(update);
        return s;
    }

}
