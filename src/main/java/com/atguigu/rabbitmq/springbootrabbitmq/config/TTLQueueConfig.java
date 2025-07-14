package com.atguigu.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.atguigu.rabbitmq.springbootrabbitmq.config
 * @Author: zhuang
 * @Date: 2025/7/9 9:44
 */
@Configuration
public class TTLQueueConfig {
    public static final String X_EXCHANGE = "X";
    public static final String X_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    public static final String DEAD_LETTER_QUEUE = "QD";

    //普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(X_DEAD_LETTER_EXCHANGE);
    }

    //普通队列
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", X_DEAD_LETTER_EXCHANGE);
        //设置死信路由键
        arguments.put("x-dead-letter-routing-key", "YD");
        //设置队列TTL  如果在ttl时间内没有被消费，则进入死信队列
        arguments.put("x-message-ttl", 10 * 1000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //普通队列
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", X_DEAD_LETTER_EXCHANGE);
        //设置死信路由键
        arguments.put("x-dead-letter-routing-key", "YD");
        //设置队列TTL  如果在ttl时间内没有被消费，则进入死信队列
        arguments.put("x-message-ttl", 40 * 1000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //普通队列
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", X_DEAD_LETTER_EXCHANGE);
        //设置死信路由键
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //队列绑定交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
