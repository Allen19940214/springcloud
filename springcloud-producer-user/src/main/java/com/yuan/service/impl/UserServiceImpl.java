package com.yuan.service.impl;

import com.yuan.dao.UserDao;
import com.yuan.pojo.User;
import com.yuan.service.UserService;
import com.yuan.utils.RequestAndResponseUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public int updateById(User user) {
        return userDao.updateById(user);
    }

    @Override
    public int deleteById(Integer id) {
        return userDao.deleteById(id);
    }

    @Override
    public String login(Map map) {
        HttpServletRequest request = RequestAndResponseUtil.getRequest();
        HttpServletResponse response = RequestAndResponseUtil.getResponse();
        User user = userDao.login(map);
        if(user==null){
            return "账号或密码错误";
        }
        HttpSession session = request.getSession();
        session.setAttribute("userSession",user);
        Cookie cookie = new Cookie("cookie_username", user.getUsername());
        cookie.setMaxAge(60*60);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
        return "登陆成功";
    }
    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }
}
