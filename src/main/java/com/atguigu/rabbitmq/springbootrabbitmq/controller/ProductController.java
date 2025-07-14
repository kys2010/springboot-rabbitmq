package com.atguigu.rabbitmq.springbootrabbitmq.controller;

import com.atguigu.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import com.atguigu.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description: com.atguigu.rabbitmq.springbootrabbitmq.controller
 * @Author: zhuang
 * @Date: 2025/7/9 14:32
 */
@RestController
@Slf4j
@RequestMapping("/confirm")
@Tag(name = "发布确认高级内容接口", description = "用于演示 RabbitMQ 中 发布确认高级的消息发送")
public class ProductController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //发布确认高级
    @GetMapping("/sendMsg/{msg}")
    @Operation(summary = "发送指定消息", description = "将一条消息发送到队列中")
    public void sendDelayMsg (
            @Parameter(description = "要发送的消息内容", example = "Hello RabbitMQ")
            @PathVariable String msg) {
        log.info("当前时间：{} 发送了一条消息给队列：{}", new Date().toString(), msg);
        CorrelationData correlation = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, msg, correlation);

        correlation = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "123", msg, correlation);
    }
}
