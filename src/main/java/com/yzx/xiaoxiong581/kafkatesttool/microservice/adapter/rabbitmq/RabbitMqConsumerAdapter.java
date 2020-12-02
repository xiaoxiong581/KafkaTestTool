package com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rabbitmq;

import com.rabbitmq.client.*;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rabbitmq.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangzhixiong
 */
@Slf4j
public class RabbitMqConsumerAdapter extends Thread {
    private Connection connection;

    private Channel channel;

    private RabbitMqConfig consumerConfig;

    private String taskName;

    public RabbitMqConsumerAdapter(String taskName, RabbitMqConfig consumerConfig) {
        this.taskName = taskName;
        this.consumerConfig = consumerConfig;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(consumerConfig.getHost());
        factory.setPort(consumerConfig.getPort());
        factory.setVirtualHost(consumerConfig.getVHost());
        factory.setUsername(consumerConfig.getUsreName());
        factory.setPassword(consumerConfig.getPassword());

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(consumerConfig.getQueueName(), true, false, false, null);
        } catch (IOException | TimeoutException e) {
            log.error("rabbitMq init consumer failed", e);
            throw new RuntimeException(e);
        }
        log.info("task {} init rabbitMq {} consumer success", taskName, consumerConfig.getHost());
    }

    @Override
    public void run() {
        try {
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    log.info("{} receive, queue: {}, key: {}, value: {}, consumerTag: {}",
                            taskName,
                            consumerConfig.getQueueName(),
                            null != properties ? properties.getMessageId() : null, new String(body, "utf-8"), consumerTag);
                }
            };
            channel.basicConsume(consumerConfig.getQueueName(), true, consumer);
        } catch (Exception e) {

        }
    }
}
