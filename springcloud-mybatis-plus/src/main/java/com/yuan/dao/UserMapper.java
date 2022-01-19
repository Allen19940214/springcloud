package com.yuan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//mybatis-plus
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
