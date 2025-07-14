package com.atguigu.rabbitmq.springbootrabbitmq.callback;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: com.atguigu.rabbitmq.springbootrabbitmq.callback
 * @Author: zhuang
 * @Date: 2025/7/9 15:02
 */
@Component
@Slf4j
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息成功发送: {}", correlationData.getId());
        } else {
            log.info("消息成功失败: {}, 原因为：{}", correlationData.getId(), cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息发送失败返回了: {}", returnedMessage);
    }
}
