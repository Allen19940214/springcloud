package com.yuan.aop;

import com.yuan.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandle implements InvocationHandler {

    //目标对象

    Object tarObj;

    public MyInvocationHandle(Object tarObj) {
        this.tarObj = tarObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke=null;
        if(method.getName().equals("addUser")||method.getName().equals("updateById")||method.getName().equals("deleteById")){
            //前置通知
            System.out.println("执行前置通知");
            //执行目标对象的方法
            invoke = method.invoke(tarObj, args);
            //后置通知
            System.out.println("后置通知");
        }else {
            invoke = method.invoke(tarObj, args);
        }
        return invoke;
    }
}
