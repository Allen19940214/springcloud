package com.yuan.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderDispatchService {
    private int count=1;
    @Autowired
    private DispatchService dispatchService;

    @RabbitListener(queues = "deadSmsQueue")
    public void getOrder(String order, Message message, Channel channel) throws IOException {
        try {
            log.info("监听到死信队列deadSmsQueue，进行派单，消息为{}",order);
            System.out.println(dispatchService.addPatchOrder());
            System.out.println(1/0);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            //如果再出现异常 则人工干预
            log.info("短信邮件警报+人工干预");
            //不管是否正常消费都最好再次确认 防止私信队列中的消息堆积（不确认在管理界面显示为Unacked状态）
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
    }
    /*
    解决消息消费异常重试方案
    1、控制重发次数
    2、try catch +手动ack 这种处理方式会覆盖配置文件中的重发次数
    3、try catch +手动ack +死信队列
     */
    @RabbitListener(queues = "ttlSmsQueue")
    public void getOrder1(String order,Message message, Channel channel) throws IOException {
        try {
            log.info("监听到正常队列ttlSmsQueue，准备派单，消息为{},count:{}",order,count++);
            System.out.println(dispatchService.addPatchOrder());
            System.out.println(1/0);
            //业务执行完 调用basicAck方法进行确认  确认消费消息成功 tag参数为消息的标签 可看成消息的唯一id
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
        /*
        出现异常 basicNack requeue是否重发(false不重发，如果配置文件配置了开启重发并设置了重发次数)
        会导致，false失效问题，可能按配置文件重发次数进行重试，也可能生效覆盖重发次数，所以使用手动ack+try catch和配置文件控制重发次数
        两者选其一即可。一般不重试，没有意义。所以可靠消费最佳解决方案：try catch +手动ack +死信队列，并且不进行重试。
        如果出现异常则发送到死信队列
         */
            /*监听到消息但是执行业务代码时（包括确认ack的时候）出现异常，都将到catch块中
            调用basicNack方法，表示确认消息消费失败，requeue（false）表示并且不再重复请求消费。
            为此队列绑定一个死信队列，则确认失败后会将消息投递到死信队列。然后为死信队列建立消费者，一样选择try catch +手动ack +死信队列
            方式重复业务代码或者选择报警人工干预等等。
            basicNack*/
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
    }
}
