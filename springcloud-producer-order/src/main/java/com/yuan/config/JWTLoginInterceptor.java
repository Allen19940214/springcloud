package com.yuan.config;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//token拦截器
@Component
@Slf4j
public class JWTLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的令牌
        Map<String, Object> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies==null||cookies.length==0){
            map.put("msg","cookie不存在");
            String json = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                log.info("当前token为：{}", token);
                try {
                    JWTUtil.verify(token);
                    log.info("token验证成功");
                    return true;
                } catch (SignatureVerificationException e) {
                    e.printStackTrace();
                    map.put("msg", "签名不一致");
                } catch (TokenExpiredException e) {
                    e.printStackTrace();
                    map.put("msg", "令牌过期");
                } catch (AlgorithmMismatchException e) {
                    e.printStackTrace();
                    map.put("msg", "算法不匹配");
                } catch (InvalidClaimException e) {
                    e.printStackTrace();
                    map.put("msg", "失效的payload");
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("msg", "token无效");
                }
                map.put("status", false);
                //响应到前台: 将map转为json
                String json = new ObjectMapper().writeValueAsString(map);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(json);
            }
        }
        return false;
    }
}
