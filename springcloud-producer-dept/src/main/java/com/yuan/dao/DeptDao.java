package com.yuan.dao;

import com.yuan.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface DeptDao {
    List<Dept> findAll();
    Dept findById(@Param("id")Integer id);
    int addDept(Dept dept);
    int updateById(Dept dept);
    int deleteById(@Param("id")Integer id);
}
