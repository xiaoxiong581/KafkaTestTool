package com.yzx.xiaoxiong581.kafkatesttool.microservice.controller.rabbitmq;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rabbitmq.RabbitMqConsumerAdapter;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rabbitmq.RabbitMqProducerAdapter;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rabbitmq.ProducerConfig;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rabbitmq.RabbitMqConfig;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

public class RabbitMqTest {
    @Test
    public void testRabbitMq() throws InterruptedException {
        RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();
        rabbitMqConfig.setHost("192.168.137.102");
        rabbitMqConfig.setPort(5672);
        rabbitMqConfig.setVHost("xiaoxiong581");
        rabbitMqConfig.setUsreName("admin");
        rabbitMqConfig.setPassword("migu@1234");
        rabbitMqConfig.setQueueName("test-queue");

        String taskName = "task_rabbitmq_test";
        RabbitMqConsumerAdapter consumerAdapter = new RabbitMqConsumerAdapter(taskName, rabbitMqConfig);

        ProducerConfig producerConfig = new ProducerConfig();
        BeanUtils.copyProperties(rabbitMqConfig, producerConfig);
        producerConfig.setMsgCount(0);
        producerConfig.setMsgLength(8);
        RabbitMqProducerAdapter producerAdapter = new RabbitMqProducerAdapter(taskName, producerConfig);

        consumerAdapter.start();
        producerAdapter.start();

        Thread.sleep(1800000);
    }
}
