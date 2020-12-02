package com.yzx.xiaoxiong581.kafkatesttool.microservice.controller.rabbitmq;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rocketmq.RocketMqConsumerAdapter;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rocketmq.RocketMqProducerAdapter;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rocketmq.ProducerConfig;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rocketmq.RocketMqConfig;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

public class RocketMqTest {
    @Test
    public void testRocketMq() throws InterruptedException {
        RocketMqConfig rocketMqConfig = new RocketMqConfig();
        rocketMqConfig.setUrl("192.168.137.102:9876");
        rocketMqConfig.setGroupName("DefaultCluster");
        rocketMqConfig.setTopic("test-topic-1");

        String taskName = "task_rocketmq_test";
        RocketMqConsumerAdapter consumerAdapter = new RocketMqConsumerAdapter(taskName, rocketMqConfig);

        ProducerConfig producerConfig = new ProducerConfig();
        BeanUtils.copyProperties(rocketMqConfig, producerConfig);
        producerConfig.setMsgCount(0);
        producerConfig.setMsgLength(8);
        RocketMqProducerAdapter producerAdapter = new RocketMqProducerAdapter(taskName, producerConfig);

        consumerAdapter.start();
        producerAdapter.start();

        Thread.sleep(1800000);
    }
}
