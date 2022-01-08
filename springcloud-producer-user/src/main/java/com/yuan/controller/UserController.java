package com.yuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yuan.pojo.User;
import com.yuan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    ObjectMapper objectMapper;
    @GetMapping("/getClientInfo")
    public String getClientInfo() {
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-producer-user");
        Iterator<ServiceInstance> iterator = instances.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        return instances.toString();
    }
    @GetMapping("/findAll")
    @HystrixCommand(fallbackMethod = "findAllHystrix")//局部熔断 处理异常+超时 熔断方法和原方法参数必须一致
    public String findAllDept() throws JsonProcessingException, InterruptedException {
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-producer-user");
        Integer port=null;
        for (ServiceInstance instance : instances) {
            port=instance.getPort();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String user = objectMapper.writeValueAsString(userService.findAll()+">>>>>>"+port.toString());
        return user;
    }
    //局部熔断方法
    public String findAllHystrix() {
        return "用户模块访问超时，网络异常";
    }
    @GetMapping("/findById/{id}")
    @HystrixCommand(fallbackMethod = "findByIdHystrix")//局部熔断 处理异常+超时
    public String findById(@PathVariable("id") Integer id) throws JsonProcessingException, InterruptedException {
        //TimeUnit.MILLISECONDS.sleep(5000);
        ObjectMapper objectMapper = new ObjectMapper();
        String user = objectMapper.writeValueAsString(userService.findById(id));
        return user;
    }
    //局部熔断方法
    public String findByIdHystrix(Integer id) {
        return "用户模块访问超时，网络异常";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) {
        int i = userService.addUser(user);
        if(i>0){
            return "添加成功";
        }
        return "添加失败";
    }
    @PostMapping("/updateById")
    public String updateById(@RequestBody User user) {
        int i = userService.updateById(user);
        if(i>0){
            return "更新成功";
        }
        return "更新失败";
    }
    @GetMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        int i = userService.deleteById(id);
        if(i>0){
            return "删除成功";
        }
        return "删除失败";
    }
    @RequestMapping ("/testFindByUserName")
    public String testFindByUserName(String username) throws JsonProcessingException {
        User byUserName = userService.findByUserName(username);
        String s = objectMapper.writeValueAsString(byUserName);
        return s;
    }
    @RequestMapping ("/loginPage")
    public ModelAndView toLoginPage()  {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @RequestMapping ("/toUserPage")
    public ModelAndView toUserPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userShow");
        return modelAndView;
    }
    @RequestMapping ("/login")
    public String login(@RequestParam("username")String username,@RequestParam("password")String password){
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        String login = userService.login(map);
        return login;
    }
    @RequestMapping ("/loginByToken")
    public String loginByToken(@RequestParam("username")String username,@RequestParam("password")String password){
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        String loginInfo = userService.loginByToken(map);
        return loginInfo;
    }
}
