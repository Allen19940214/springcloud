package com.yuan.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
//利用redis实现分布式锁案例
@RestController
public class MsController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Redisson redisson;
    @RequestMapping("/deductStock")
    public String  deductStock(){
        //分布式锁 扣库存案例，不考虑性能能问题，单体架构加锁即可解决，但是如果是分布式会出现问题
        synchronized (this){
            String s = stringRedisTemplate.opsForValue().get("stock");
            int stock = Integer.parseInt(s);
            if(stock>0){
                int laveStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock",laveStock+"");//库存减一
                System.out.println("扣减成功，剩余库存："+laveStock+"");
            }else {
                System.out.println("库存不足");
            }
        }
        return "end";
    }

    //分布式锁解决线程不安全的问题
    @RequestMapping("/deductStockLock1")
    public String  deductStockLock1(){
        //setIfAbsent相当于setnx命令 与set命令不同的是。set已经存在的值。value会覆盖原先的之，setnx 如果存在key 则不做修改，并返回 true或false
        //进来先上锁 利用redis单线程模型，排队执行，就算高并发会有多个请求同时到达redis 最终只有一个线程修改成功(返回flase)，跳过if判断执行减库存操作
        //在操作完后释放锁
        //Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(orderuuid, "zz");//相当于setnx
        //设置过期时间，避免死锁（接口永远不能访问，不能对商品进行抢购）还有问题设置锁和超时放在两句话也不安全 可以设置锁时同时设置过期时间
        //stringRedisTemplate.expire(orderuuid,10, TimeUnit.SECONDS);
        String orderuuid="orderuuid";
        String clientId = UUID.randomUUID().toString();
        try {
            /*
            高并发，会导致锁失效，执行慢的线程，解锁时他自己本身上的锁已经失效，而删除的是别的线程上的锁，所以可以加个线程uuid 作为value
            释放锁时判断锁是否属于当前的线程， 因为网络不可靠和不可预判，或者程序执行的时间不确定等因素，
            设置的过期时间在真实高并发场景也有可能出现问题，所以在高要求情况下使用redisson
            */
            Boolean zz = stringRedisTemplate.opsForValue().setIfAbsent(orderuuid, clientId, 10, TimeUnit.SECONDS);
            if(zz==false){
                return "error";
            }
            String s = stringRedisTemplate.opsForValue().get("stock");
            int stock = Integer.parseInt(s);
            if(stock>0){
                int laveStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock",laveStock+"");//库存减一
                System.out.println("扣减成功，剩余库存："+laveStock+"");
            }else {
                System.out.println("库存不足");
            }
        }finally {
            if(clientId.equals(stringRedisTemplate.opsForValue().get(orderuuid))){
                stringRedisTemplate.delete("orderuuid");
            }
        }
        return "end";
    }
    //使用redisson
    @RequestMapping("/deductStockLock")
    public String  deductStockLock(){
        String orderuuid="orderuuid";
        RLock redissonLock = redisson.getLock(orderuuid);
        try {
            redissonLock.lock(30,TimeUnit.SECONDS);
            String s = stringRedisTemplate.opsForValue().get("stock");
            int stock = Integer.parseInt(s);
            if(stock>0){
                int laveStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock",laveStock+"");//库存减一
                System.out.println("扣减成功，剩余库存："+laveStock+"");
            }else {
                System.out.println("库存不足");
            }
        }finally {
            redissonLock.unlock();
        }
        return "end";
    }
}
