package com.yuan.aop;

import com.yuan.utils.RequestAndResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class AnnotationLogAspect {
    @Pointcut("@annotation(com.yuan.aop.LogAnnotation)")
    private void aspect(){}
    //环绕通知
    @Around("aspect()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long starterTime = System.currentTimeMillis();
        //执行目标对象原有方法
        Object result = joinPoint.proceed();
        long time=System.currentTimeMillis()-starterTime;
        //记录结束
        recordLog(joinPoint,time);
        return result;
    }
    //日志记录方法
    private void recordLog(ProceedingJoinPoint joinPoint,long time){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Signature pointSignature = joinPoint.getSignature();
        String methodName=joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        //请求方式
        String requestMethod = RequestAndResponseUtil.getRequest().getMethod();
        String requestURI = RequestAndResponseUtil.getRequest().getRequestURI();
        String remoteAddr = RequestAndResponseUtil.getRequest().getRemoteAddr();
        log.info("=========================log start===========================");
        log.info("访问module:{}",logAnnotation.module());
        log.info("操作operator:{}",logAnnotation.operator());
        log.info("访问方法名:{}",methodName);
        log.info("访问类名:{}",className);
        log.info("返回值+类方法+参数类型:{}",pointSignature);
        log.info("访问参数:{}", Arrays.toString(args));
        log.info("访问方式:{}",requestMethod);
        log.info("URI:{}",requestURI);
        log.info("IP地址:{}",remoteAddr);
        log.info("=========================log end=============================");
        log.info("耗时:{}ms",time);
    }
}
