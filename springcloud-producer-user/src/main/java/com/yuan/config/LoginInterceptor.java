package com.yuan.config;

import com.yuan.dao.UserDao;
import com.yuan.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//cookie+session拦截器
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserDao userDao;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            response.sendRedirect(request.getContextPath()+"/loginPage");
            log.error("用户没有cookie，请登录");
            return false;
        }
        String cookieUsername=null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("cookie_username")){
                log.info("cookie检查通过，开始查询用户session");
                cookieUsername = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(cookieUsername)) {
            log.error("用户cookie验证失败，请重新登录");
            response.sendRedirect(request.getContextPath() + "/loginPage");
            return false;
        }
        HttpSession session = request.getSession();
        Object userSession = session.getAttribute("userSession");
        if(userSession!=null){
            log.info("获得用户session>>>>>>>>"+userSession.toString());
        }
        if(userSession==null){
            User user = userDao.findByUserName(cookieUsername);
            session.setAttribute("userSession",user);
        }
        //已经登陆 放行
        return true;
    }
}
