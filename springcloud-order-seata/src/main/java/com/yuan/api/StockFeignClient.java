package com.yuan.api;

import com.yuan.pojo.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "springcloud-stock-seata")
@RequestMapping("/stock")
public interface StockFeignClient {
    @RequestMapping("/selectStockByName")
    String selectStockByName(@RequestParam("goodName") String name);

    @RequestMapping("/updateStockByName")
    String updateStockByName(@RequestBody Stock stock);
}
