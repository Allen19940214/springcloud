package com.yuan.service;

import com.yuan.pojo.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DeptClientServiceFallback implements FallbackFactory {
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {
            @Override
            public String findAllDept() {
                return "服务暂时不可用";
            }

            @Override
            public String findById(Integer id) {
                return "服务暂时不可用";
            }

            @Override
            public String addDept(Dept dept) {
                return "服务暂时不可用";
            }

            @Override
            public String updateById(Dept dept) {
                return "服务暂时不可用";
            }

            @Override
            public String deleteById(Integer id) {
                return "服务暂时不可用";
            }
        };
    }
}
