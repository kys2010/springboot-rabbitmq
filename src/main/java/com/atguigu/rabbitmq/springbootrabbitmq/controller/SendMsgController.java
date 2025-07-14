package com.atguigu.rabbitmq.springbootrabbitmq.controller;

import com.atguigu.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Description: com.atguigu.rabbitmq.springbootrabbitmq.controller
 * @Author: zhuang
 * @Date: 2025/7/9 10:01
 */
@RestController
@RequestMapping("/ttl")
@Tag(name = "TTL 消息发送接口", description = "用于演示 RabbitMQ 中 TTL（存活时间）队列的消息发送")
@Slf4j
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Operation(summary = "发送消息到两个固定 TTL 队列", description = "将一条消息分别发送到 TTL 为 10s 和 4")
    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(
            @Parameter(description = "要发送的消息内容", example = "Hello RabbitMQ")
            @PathVariable String msg){
        log.info("当前时间：{} 发送了一个条消息给两个TTL队列：{}", new Date().toString(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10s 的队列：" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40s 的队列：" + msg);
    }

    //生产消息的时候设置TTL
    @GetMapping("/sendExpMsg/{msg}/{ttl}")
    @Operation(summary = "发送消息到需要指定TTL的队列", description = "将一条消息分别发送到指定队列并指定TTL")
    public void sendExpMsg(
            @Parameter(description = "要发送的消息内容", example = "Hello RabbitMQ")
            @PathVariable String msg,
            @Parameter(description = "消息存活时间（单位：毫秒）", example = "5000")
            @PathVariable String ttl){
        log.info("当前时间：{} 发送了一条时长为{}毫秒的消息给两个TTL队列：{}", new Date().toString(),ttl, msg);
        rabbitTemplate.convertAndSend("X", "XC", msg, message -> {
            //设置消息的TTL
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });
    }

    //发送延迟消息并指定延迟时间
    @GetMapping("/sendDelayMsg/{msg}/{delayTime}")
    @Operation(summary = "发送延迟消息并指定延迟时间", description = "将一条消息分别发送到延迟队列并指定延迟时间")
    public void sendDelayMsg(
            @Parameter(description = "要发送的消息内容", example = "Hello RabbitMQ")
            @PathVariable String msg,
            @Parameter(description = "消息延迟时间（单位：毫秒）", example = "5000")
            @PathVariable Integer delayTime){
        log.info("当前时间：{} 发送了一条延迟时间为{}毫秒的消息给延迟队列：{}", new Date().toString(),delayTime, msg);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, msg, message -> {
            //设置消息的TTL
            message.getMessageProperties().setDelay(delayTime);
            return message;
        });
    }

}
