package com.yuan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuan.dao.OrderDao;
import com.yuan.pojo.Order;
import com.yuan.service.OrderService;
import com.yuan.service.task.TimingTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService ,RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
    @Autowired
    //@Qualifier("rabbitTemplate1")
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    TimingTaskService timingTaskService;
    @PostConstruct
    public void init(){
        //将自定义接口实现类注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ObjectMapper objectMapper;
    //分页查询
    @Override
    public List<Order> findAll(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderDao.findAll();
        PageInfo<Order> orderPageInfo = new PageInfo<>(orderList);
        return orderPageInfo.getList();
    }
    @Override
    public Order findById(String id) {
        return orderDao.findById(id);
    }

    @Override
    public int addOrder(Order order) throws JsonProcessingException {
        int i = orderDao.addOrder(order);
        if(i>0){
            CorrelationData correlationData = new CorrelationData();
            String s = objectMapper.writeValueAsString(order);
            byte[] bytesOrder = s.getBytes();
            correlationData.setReturnedMessage(new Message(bytesOrder));
            rabbitTemplate.convertAndSend("ttlDirectExchange11111","ttlsms",objectMapper.writeValueAsString(order),correlationData);
            return i;
        }
        return 0;
    }

    @Override
    public int updateById(Order order) {
        return orderDao.updateById(order);
    }

    @Override
    public int deleteById(String id) {
        return orderDao.deleteById(id);
    }

    @Override
    public int addOrderToBackup(Order order) {
        return orderDao.addOrderToBackup(order);
    }

    @Override
    public int updateByIdBackup(Order order) {
        return orderDao.updateByIdBackup(order);
    }

    @Override
    public List<Order> selectByCondition(Map map) {
        return orderDao.selectByCondition(map);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(!ack){
            try {
                //获取订单
                log.error("订单发送失败:{},失败原因为:{}",ack,cause);
                byte[] body = correlationData.getReturnedMessage().getBody();
                String sOrder = new String(body);
                Order order = objectMapper.readValue(sOrder, new TypeReference<Order>() {});
                log.info("发送的消息体为：{}",order);
                order.setMqStatus(0);
                orderService.addOrderToBackup(order);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("消息发送失败，并且添加冗余消息是出现异常");
            }
        }
        //成功，也需将消息添加到本地消息冗余表或者更新状态
        if(ack){
            try {
                //获取订单
                log.info("订单发送成功:{},成功cause为:{}",ack,cause);
                byte[] body = correlationData.getReturnedMessage().getBody();
                String sOrder = new String(body);
                Order order = objectMapper.readValue(sOrder, new TypeReference<Order>() {});
                log.info("发送的消息体为：{}",order);
                order.setMqStatus(1);
                Map map =new HashMap<>();
                map.put("orderId", order.getOrderId());
                orderService.updateById(order);
                orderService.addOrderToBackup(order);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("消息发送成功，但是添加冗余消息是出现异常");
            }
        }
    }
    /*
    Mandatory参数
    消息确认机制为交换机的机制，只要交换机收到消息，便视为成功，但是交换机将消息路由给队列时出现问题，消息也将会丢失，
    所以可以使用消息回退，即如果交换机在将消息路由到队列时出现问题，导致队列没有收到消息，交换机需要将消息回退给生产者。
    需配置publisher-returns: true ，并实现回退接口
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //走到这里说明交换机路由到队列时肯定失败 可选择做一些操作 对退回的消息做标记
        log.error("消息{}，被交换机{}退回，退回原因为：{}，路由key：{}",
                new String(message.getBody()),exchange,replyText,routingKey);
    }
}
