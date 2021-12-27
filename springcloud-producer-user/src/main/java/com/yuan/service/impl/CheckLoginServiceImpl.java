package com.yuan.service.impl;

import com.yuan.pojo.User;
import com.yuan.service.CheckLoginService;
import com.yuan.service.UserService;
import com.yuan.utils.RequestAndResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class CheckLoginServiceImpl implements CheckLoginService {
    @Autowired
    private UserService userService;
    @Override
    public boolean checkUser() throws IOException {
        HttpServletRequest request = RequestAndResponseUtil.getRequest();
        HttpServletResponse response = RequestAndResponseUtil.getResponse();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("username")){
                    User user = userService.findByUserName(cookie.getValue());
                    if(user!=null){
                        log.info("你登录过~~~");
                        return true;
                    }
                    log.error("cookie验证失败");
                    response.sendRedirect(request.getContextPath()+"/loginPage");
                }
            }
        }else {
            log.error("用户没有cookie请登录");
            response.sendRedirect(request.getContextPath()+"/loginPage");
        }
        return false;
    }
}
