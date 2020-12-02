package com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rocketmq;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rocketmq.RocketMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author yangzhixiong
 */
@Slf4j
public class RocketMqConsumerAdapter extends Thread {
    private DefaultMQPushConsumer consumer;

    private String taskName;

    public RocketMqConsumerAdapter(String taskName, RocketMqConfig consumerConfig) {
        this.taskName = taskName;
        consumer = new DefaultMQPushConsumer(consumerConfig.getGroupName());
        consumer.setNamesrvAddr(consumerConfig.getUrl());

        try {
            consumer.subscribe(consumerConfig.getTopic(), "*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    msgs.forEach(msg -> log.info("{} receive, topic: {}, msgId: {}, value: {}",
                            taskName,
                            consumerConfig.getTopic(),
                            msg.getMsgId(), new String(msg.getBody())));
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        } catch (Exception e) {
            log.error("rabbitMq init consumer failed", e);
            throw new RuntimeException(e);
        }
        log.info("task {} init rocketMq {} consumer success", taskName, consumerConfig.getUrl());
    }

    @Override
    public void run() {
        try {
            consumer.start();
        } catch (Exception e) {
            log.error("{} catch exception when start consumer, exception: {}", taskName, e.getMessage());
        }
    }
}
