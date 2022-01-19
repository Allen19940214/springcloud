package com.yuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.dao.UserMapper;
import com.yuan.pojo.User;
import com.yuan.service.UserMapperService;
import org.springframework.stereotype.Service;

@Service
public class UserMapperServiceImpl extends ServiceImpl<UserMapper, User> implements UserMapperService {

}
