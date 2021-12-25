package com.yuan.service;

import com.yuan.pojo.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
    Dept findById(Integer id);
    int addDept(Dept dept);
    int updateById(Dept dept);
    int deleteById(Integer id);
}
