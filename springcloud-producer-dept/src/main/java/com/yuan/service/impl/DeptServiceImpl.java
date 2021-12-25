package com.yuan.service.impl;

import com.yuan.dao.DeptDao;
import com.yuan.pojo.Dept;
import com.yuan.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    DeptDao deptDao;
    @Override
    public List<Dept> findAll() {
        return deptDao.findAll();
    }

    @Override
    public Dept findById(Integer id) {
        return deptDao.findById(id);
    }

    @Override
    public int addDept(Dept dept) {
       return deptDao.addDept(dept);
    }

    @Override
    public int updateById(Dept dept) {
       return deptDao.updateById(dept);
    }

    @Override
    public int deleteById(Integer id) {
       return deptDao.deleteById(id);
    }
}
