package com.yuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.UserMapper;
import com.yuan.pojo.User;
import com.yuan.service.UserMapperService;
import com.yuan.utils.JWTUtil;
import com.yuan.utils.RequestAndResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserMapperServiceImpl extends ServiceImpl<UserMapper, User> implements UserMapperService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User login(String name, String password) {
        QueryWrapper<User> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("username",name).eq("password",password);
        User one = this.getOne(QueryWrapper);
        if(one==null){
            return null;
        }
        HttpServletRequest request = RequestAndResponseUtil.getRequest();
        HttpServletResponse response = RequestAndResponseUtil.getResponse();
        Map map=new HashMap<>();
        map.put("username",one.getUsername());
        String token = JWTUtil.getToken(map);
        Cookie cookie = new Cookie("token",token);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);
        return one;
    }

    @Override
    @Transactional
    public boolean update(User entity, Wrapper<User> updateWrapper) {
        int update = userMapper.update(entity, updateWrapper);
        //int i=1/0;
        if(update==0){
            return false;
        }
        return true;
    }
}
