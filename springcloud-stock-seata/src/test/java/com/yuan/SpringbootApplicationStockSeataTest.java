package com.yuan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuan.dao.StockMapper;
import com.yuan.pojo.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootApplicationStockSeataTest {
    @Autowired
    StockMapper stockMapper;
    @Test
    public void test1(){
        stockMapper.selectList(null).toString();
    }
    @Test
    public void test2(){
        QueryWrapper<Stock> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("good_name","洗衣液");
        stockMapper.selectOne(QueryWrapper);
    }
}
