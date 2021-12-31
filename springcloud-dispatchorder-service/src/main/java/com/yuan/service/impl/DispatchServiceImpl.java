package com.yuan.service.impl;

import com.yuan.service.DispatchService;
import org.springframework.stereotype.Service;

@Service
public class DispatchServiceImpl implements DispatchService {
    @Override
    public String addPatchOrder() {
        return "进行派单";
    }
}
