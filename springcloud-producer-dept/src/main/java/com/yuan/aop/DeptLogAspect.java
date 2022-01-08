package com.yuan.aop;

import com.yuan.utils.RequestAndResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.SimpleTimeZone;

@Component
@Aspect
@Slf4j
public class DeptLogAspect {

    @Pointcut("execution(* com.yuan.controller.*.*(..))")
    private void aspect(){}

    @Around("aspect()")
    public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
        //记录开始时间
        Long starterTime = System.currentTimeMillis();
        String format = new SimpleDateFormat().format(new Date(starterTime));
        //返回值+全类名+方法名(参数类型)
        Signature signature = joinPoint.getSignature();
        //方法所属类的全类名
        String declaringTypeName = signature.getDeclaringTypeName();
        //方法所属类简单类名
        String simpleName = signature.getDeclaringType().getSimpleName();
        //方法名
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = RequestAndResponseUtil.getRequest();
        //请求方式
        String requestMethod = request.getMethod();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("访问接口发生错误",throwable);
        }
        log.info("=================log stater====================");
        log.info("请求时间:{}",format);
        log.info("请求IP:{}",request.getRemoteAddr());
        log.info("请求URI:{}",request.getRequestURI());
        log.info("请求方式:{}",requestMethod);
        log.info("简单类名:{}",simpleName);
        log.info("全类名:{}",declaringTypeName);
        log.info("返回值+全类名(参数类型):{}",signature);
        log.info("方法名:{}",methodName);
        log.info("参数:{}", Arrays.toString(joinPoint.getArgs()));
        log.info("耗时:{}ms",System.currentTimeMillis()-starterTime);
        log.info("=================log end====================");
        return result;
    }
}
