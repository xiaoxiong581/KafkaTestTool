package com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rabbitmq.ProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author yangzhixiong
 */
@Slf4j
public class RabbitMqProducerAdapter extends Thread {
    private Connection connection;

    private Channel channel;

    private ProducerConfig producerConfig;

    private String taskName;

    public RabbitMqProducerAdapter(String taskName, ProducerConfig producerConfig) {
        this.taskName = taskName;
        this.producerConfig = producerConfig;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(producerConfig.getHost());
        factory.setPort(producerConfig.getPort());
        factory.setVirtualHost(producerConfig.getVHost());
        factory.setUsername(producerConfig.getUsreName());
        factory.setPassword(producerConfig.getPassword());

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(producerConfig.getQueueName(), true, false, false, null);
        } catch (IOException | TimeoutException e) {
            log.error("rabbitMq init producer failed", e);
            throw new RuntimeException(e);
        }
        log.info("task {} init rabbitMq {} producer success", taskName, producerConfig.getHost());
    }

    @Override
    public void run() {
        int sendMessageNum = producerConfig.getMsgCount();
        int valueLength = producerConfig.getMsgLength();

        String value = RandomStringUtils.randomAlphanumeric(valueLength);
        if (0 == sendMessageNum) {
            while (!Thread.currentThread().isInterrupted()) {
                sendMessage(value);
            }
        }

        for (int i = 0; i < sendMessageNum; i++) {
            sendMessage(value);
        }

        try {
            if (null != channel) {
                channel.close();
            }
            if (null != connection) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }

    private void sendMessage(String value) {
        String key = UUID.randomUUID().toString();
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        try {
            channel.basicPublish("", producerConfig.getQueueName(), builder.messageId(key).build(), value.getBytes(StandardCharsets.UTF_8));
            log.info("{} send, queue: {}, key: {}, value: {}",
                    taskName, producerConfig.getQueueName(), key, value);
            if (producerConfig.getSendTimeInterval() > 0) {
                Thread.sleep(producerConfig.getSendTimeInterval() * 1000);
            }
        } catch (InterruptedException e) {

        } catch (Exception e) {
            log.error("{} catch exception when s, queue: {}, key: {}, value: {}, exception: {}", taskName, producerConfig.getQueueName(), key, value,
                    e.getMessage());
        }
    }
}
