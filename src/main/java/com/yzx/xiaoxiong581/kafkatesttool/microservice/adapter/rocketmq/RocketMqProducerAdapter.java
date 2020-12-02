package com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.rocketmq;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rocketmq.ProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author yangzhixiong
 */
@Slf4j
public class RocketMqProducerAdapter extends Thread {
    private DefaultMQProducer producer;

    private ProducerConfig producerConfig;

    private String taskName;

    public RocketMqProducerAdapter(String taskName, ProducerConfig producerConfig) {
        this.taskName = taskName;
        this.producerConfig = producerConfig;
        producer = new
                DefaultMQProducer(producerConfig.getGroupName());
        producer.setNamesrvAddr(producerConfig.getUrl());
        try {
            producer.start();
        } catch (Exception e) {
            log.error("rocketMq init producer failed", e);
            throw new RuntimeException(e);
        }
        log.info("task {} init rocketMq {} producer success", taskName, producerConfig.getUrl());
    }

    @Override
    public void run() {
        int sendMessageNum = producerConfig.getMsgCount();
        int valueLength = producerConfig.getMsgLength();

        String value = RandomStringUtils.randomAlphanumeric(valueLength);
        try {
            if (0 == sendMessageNum) {
                while (!Thread.currentThread().isInterrupted()) {
                    sendMessage(value);
                }
            }

            for (int i = 0; i < sendMessageNum; i++) {
                sendMessage(value);
            }
        } catch (InterruptedException e) {
            log.info("taskName {} producer receive stop signal, stop task", taskName);
        }

        producer.shutdown();
    }

    private void sendMessage(String value) throws InterruptedException {
        try {
            Message msg = new Message(producerConfig.getTopic(), value.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            if (SendStatus.SEND_OK == sendResult.getSendStatus()) {
                log.info("{} send, topic: {}, msgId: {}, value: {}", taskName, producerConfig.getTopic(), sendResult.getMsgId(), value);
            }

            if (producerConfig.getSendTimeInterval() > 0) {
                Thread.sleep(producerConfig.getSendTimeInterval() * 1000);
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} catch exception when s, topic: {}, value: {}, exception: {}", taskName, producerConfig.getTopic(), value,
                    e.getMessage());
        }
    }
}
