package com.yuan.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@Slf4j
public class OrderDispatchService {
    private int count=1;
    @Autowired
    private DispatchService dispatchService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = "deadSmsQueue")
    public void getOrder(Message message, Channel channel) throws IOException {
        try {
            //死信队列同样重复代码，如果依旧出现异常则在catch块进行人工干预（或者没必要再次尝试也可根据业务进行其他操作），
            String orderMessageId = message.getMessageProperties().getMessageId();
            //从redis取出唯一id
            String redisOrderUUID= stringRedisTemplate.opsForValue().get("orderMessageId");
            if(redisOrderUUID!=null&&!orderMessageId.equals(redisOrderUUID)){
                //等于空说名没消费 进行业务
                System.out.println(dispatchService.addPatchOrder());
                //完毕将消息的id 保存到redis
                stringRedisTemplate.opsForValue().set("orderMessageId",orderMessageId);
                int i=1/0;
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
        } catch (Exception e) {
            log.error("请及时检查并进行人工补偿");
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
    public void getOrder1(Message message, Channel channel) throws IOException {
        try {
            String sOrder = new String(message.getBody());
            log.info("正常消费:{}",sOrder);
            //幂等性问题解决，先拿到发送消息时设置的全局唯一id
            String orderMessageId = message.getMessageProperties().getMessageId();
            //从redis取出唯一id
            String redisOrderUUID= stringRedisTemplate.opsForValue().get("orderMessageId");
            //简单判空 会影响别的用户正常消费 必须精确判断
            if(redisOrderUUID!=null&&!orderMessageId.equals(redisOrderUUID)){
                //满足条件说明没消费 进行业务
                System.out.println(dispatchService.addPatchOrder());
                //完毕将消息的id 保存到redis
                stringRedisTemplate.opsForValue().set("orderMessageId",orderMessageId);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                //否则是确认为同一条消息 说明已经保存过 并且已经被消费了 直接ack就不会重复执行上面的代码 避免了重复消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
            /*
            业务执行完 调用basicAck方法进行确认  确认消费消息成功 tag参数为消息的标签 可看成消息的唯一id 从1开始 重启消费端会重置为1
            如果要解决幂等性问题 不建议使用这个做判断，可以用message手动给消息设置（默认为空）唯一id并结合redis解决
             */
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
