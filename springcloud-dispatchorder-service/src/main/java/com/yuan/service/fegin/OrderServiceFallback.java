package com.yuan.service.fegin;

import com.yuan.pojo.Order;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderServiceFallback implements FallbackFactory {
    @Override
    public Object create(Throwable throwable) {
        return new FeignClientOrderService() {
            @Override
            public String addOrder(Order order) {
                return "服务降级了";
            }

            @Override
            public String findAll() {
                return "服务降级了";
            }

            @Override
            public String findById(String id) {
                return "服务降级了";
            }

            @Override
            public String updateById(Order order) {
                return "服务降级了";
            }

            @Override
            public String deleteById(String id) {
                return "服务降级了";
            }

            @Override
            public String addOrderToBackup(Order order) {
                return "服务降级了";
            }

            @Override
            public String updateByIdBackup(Order order) {
                return "服务降级了";
            }

            @Override
            public String selectByCondition(Map map) {
                return "服务降级了";
            }
        };
    }
}
