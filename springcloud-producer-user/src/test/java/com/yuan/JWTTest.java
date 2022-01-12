package com.yuan;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//jwt测试
@SpringBootTest
//@RunWith(SpringRunner.class)
public class JWTTest {
    //生成token 有头部 载体 签名组成
   /* @Test
    public void testJWT(){
        //默认头部
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND,60);
        String token = JWT.create()
                .withClaim("userId", 2)//payload
                .withClaim("username", "张三")
                .withExpiresAt(instance.getTime())//指定过期时间
                .sign(Algorithm.HMAC256("@*^.jsdOp"));//签名部分 Algorithm算法类提供很多 设置密钥
        System.out.println(token);
    }
    //验证过程
    @Test
    public void testJWT1(){
        //创建验签对象 使用加密时同样的算法
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("@*^.jsdOp")).build();
        //验证通过后产生解码对象 可以拿出负载信息
        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDE2NDM5NzcsInVzZXJJZCI6MiwidXNlcm5hbWUiOiLlvKDkuIkifQ.UsIDaloha028wcQklIzmByFytRQTKJi1O8vNHlchWtk");
        System.out.println(verify.getClaim("userId").asInt());
        System.out.println(verify.getClaim("username").asString());
    }*/
}
