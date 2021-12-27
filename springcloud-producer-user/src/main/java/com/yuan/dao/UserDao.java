package com.yuan.dao;

import com.yuan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserDao {
    List<User> findAll();
    User findById(@Param("uid")Integer id);
    int addUser(User user);
    int updateById(User user);
    int deleteById(@Param("uid")Integer id);
    //登录
    User login(Map map);
    User findByUserName(@Param("username") String username);
}
