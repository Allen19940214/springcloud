package com.yuan;

import com.yuan.aop.MyInvocationHandle;
import com.yuan.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Proxy;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AopTest {
    @Autowired
    UserDao userDao;
    @Autowired
    MyInvocationHandle myInvocationHandle;

    @Test
    public void testAop(){
        myInvocationHandle.setTarObj(userDao);
        //生成代理类
        Object proxyInstance = Proxy.newProxyInstance(userDao.getClass().getClassLoader(), userDao.getClass().getInterfaces(),myInvocationHandle);
        UserDao proxyInstance1 = (UserDao) proxyInstance;
        System.out.println(proxyInstance1.deleteById(4));
    }
}
