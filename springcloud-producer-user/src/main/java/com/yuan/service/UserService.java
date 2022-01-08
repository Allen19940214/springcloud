package com.yuan.service;

import com.yuan.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> findAll();
    User findById(@Param("uid")Integer id);
    int addUser(User user);
    int updateById(User user);
    int deleteById(@Param("uid")Integer id);
    String login(Map map);
    User findByUserName(String username);
    String loginByToken(Map map);
}
